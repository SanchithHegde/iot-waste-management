mod config;
mod mqtt_client;

use std::{
    sync::Arc,
    time::{Duration, SystemTime},
};

use anyhow::{Context, Result};
use log::{debug, error, info, trace};
use rppal::{
    gpio::{Gpio, InputPin, OutputPin},
    system::DeviceInfo,
};

const SONIC_SPEED: f64 = 34300_f64;

// Set GPIO pin numbers according to BCM pin numbering
const GPIO_TRIGGER: u8 = 18;
const GPIO_ECHO: u8 = 24;

async fn handle_signals(signals: signal_hook_tokio::Signals) {
    use futures::stream::StreamExt;
    use signal_hook::consts::signal::{SIGINT, SIGQUIT, SIGTERM};

    let mut signals = signals.fuse();
    while let Some(signal) = signals.next().await {
        match signal {
            SIGINT => {
                info!("Received signal SIGINT, quitting ...");
                std::process::exit(1);
            }
            SIGQUIT => {
                info!("Received signal SIGQUIT, quitting ...");
                std::process::exit(1);
            }
            SIGTERM => {
                info!("Received signal SIGTERM, quitting ...");
                std::process::exit(1);
            }
            _ => unreachable!(),
        }
    }
}

async fn distance(
    trigger_pin: &mut OutputPin,
    echo_pin: &mut InputPin,
    timeout: Duration,
) -> Result<f64> {
    // Build an Interval instance for a duration of 10 microseconds
    let mut interval = tokio::time::interval(Duration::from_micros(10));

    // Set `GPIO_TRIGGER` to HIGH
    trigger_pin.set_high();

    // Set `GPIO_TRIGGER` to LOW after a delay of 10 microseconds
    interval.tick().await;
    trigger_pin.set_low();

    let mut start_time = SystemTime::now();
    let mut end_time = SystemTime::now();

    // Save start_time
    while echo_pin.is_low() {
        // Assume sensor is disconnected if the `GPIO_ECHO` pin stays LOW for a duration more than
        // the timeout duration.
        if start_time.duration_since(end_time)? >= timeout {
            anyhow::bail!(
                "Failed to communicate with the sensor. \
                Please ensure a proper connection, and that the sensor works properly."
            );
        }

        start_time = SystemTime::now();
    }

    // Save arrival_time
    while echo_pin.is_high() {
        end_time = SystemTime::now();
    }

    // Duration between start and arrival
    let elapsed_time = end_time.duration_since(start_time)?;
    trace!(
        "Start time: {:?}, end time: {:?}, elapsed time: {:?}",
        start_time,
        end_time,
        elapsed_time
    );

    // Multiply with sonic speed (34300 cm/s) and then divide by 2, as the wave hits and returns
    // back.
    let distance = (elapsed_time.as_secs_f64() * SONIC_SPEED) / 2_f64;

    Ok(distance)
}

#[allow(unreachable_code, unused_variables)]
async fn run() -> Result<()> {
    use signal_hook::consts::signal::{SIGINT, SIGQUIT, SIGTERM};

    // Initialize timed logger
    pretty_env_logger::init_timed();

    // Register signal handlers
    let signals = signal_hook_tokio::Signals::new(&[SIGTERM, SIGINT, SIGQUIT])?;
    let handle = signals.handle();
    let signals_task = tokio::spawn(handle_signals(signals));
    trace!("Registered signal handlers");

    // Print device information
    info!(
        "Device: {}",
        DeviceInfo::new()
            .with_context(|| "Failed to obtain device information")?
            .model()
    );

    // Build path to configuration file
    let project_dir = directories::ProjectDirs::from("", "", "IoT-Waste-Management");
    if project_dir.is_none() {
        anyhow::bail!("Failed to obtain configuration directory path from operating system");
    }
    let config_file = project_dir.unwrap().config_dir().join("config.toml");

    // Read config from file
    let config = std::fs::read_to_string(&config_file).with_context(|| {
        format!(
            r#"Failed to read configuration file: "{}""#,
            &config_file.display()
        )
    })?;
    let config: config::Config = toml::from_str(&config)?;
    trace!(
        r#"Read config {:?} from file "{}""#,
        &config,
        &config_file.display()
    );

    // Acquire access to the Pi's GPIO peripherals
    let gpio = Gpio::new().with_context(|| "Failed to obtain GPIO peripherals")?;
    trace!("Acquired access to GPIO peripherals");

    // Setup the `GPIO_TRIGGER` and `GPIO_ECHO` pins as output and input pins, respectively
    let mut trigger_pin = gpio
        .get(GPIO_TRIGGER)
        .with_context(|| format!("Failed to get GPIO pin {}", GPIO_TRIGGER))?
        .into_output();
    trace!("Setup pin {} as output pin", GPIO_TRIGGER);

    let mut echo_pin = gpio
        .get(GPIO_ECHO)
        .with_context(|| format!("Failed to get GPIO pin {}", GPIO_ECHO))?
        .into_input();
    trace!("Setup pin {} as input pin", GPIO_ECHO);

    // Build an Interval instance for a duration of `config.delay` seconds
    let mut interval = tokio::time::interval(Duration::from_secs(config.delay));

    // Timeout for calculating distance, beyond which sensor is assumed to be not connected
    let timeout = Duration::from_millis(config.measurement_timeout);

    // Build MQTT client
    let client = Arc::new(mqtt_client::MqttClient::from_config(&config)?);

    loop {
        // Measure distance using sensor
        let distance = distance(&mut trigger_pin, &mut echo_pin, timeout)
            .await
            .with_context(|| "Failed to find distance")?;
        debug!("Measured distance: {:.1} cm", distance);

        if distance <= config.threshold_distance as f64 {
            let client = Arc::clone(&client);

            let handle: tokio::task::JoinHandle<Result<()>> = tokio::task::spawn(async move {
                client.publish_message(format!("{}", distance)).await?;
                Ok(())
            });

            (handle.await?)?;
        }

        // Wait for the interval to complete
        interval.tick().await;
    }

    handle.close();
    signals_task.await?;

    Ok(())
}

#[tokio::main]
async fn main() {
    if let Err(err) = run().await {
        error!("{}", err);
        for err in err.chain().skip(1) {
            error!("Caused by: {}", err);
        }
    }
}
