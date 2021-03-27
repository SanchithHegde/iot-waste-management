use std::time::{Duration, SystemTime};

use anyhow::Result;
use rppal::gpio::{Gpio, InputPin, OutputPin};
use rppal::system::DeviceInfo;

const SONIC_SPEED: f64 = 34300_f64;

// Set GPIO pin numbers according to BCM pin numbering
const GPIO_TRIGGER: u8 = 18;
const GPIO_ECHO: u8 = 24;

fn main() -> Result<()> {
    // Print device information
    println!("Device: {}", DeviceInfo::new()?.model());

    // Acquire access to the Pi's GPIO peripherals
    let gpio = Gpio::new()?;

    // Setup the `GPIO_TRIGGER` and `GPIO_ECHO` pins as output and input pins, respectively
    let mut trigger_pin = gpio.get(GPIO_TRIGGER)?.into_output();
    let mut echo_pin = gpio.get(GPIO_ECHO)?.into_input();

    loop {
        let distance = distance(&mut trigger_pin, &mut echo_pin)?;

        println!("Measured distance: {:.1} cm", distance);
        std::thread::sleep(Duration::from_secs(1));
    }

    Ok(())
}

fn distance(trigger_pin: &mut OutputPin, echo_pin: &mut InputPin) -> Result<f64> {
    // Set `GPIO_TRIGGER` to HIGH
    trigger_pin.set_high();

    // Set `GPIO_TRIGGER` to LOW after a delay of 10 microseconds
    std::thread::sleep(Duration::from_micros(10));
    trigger_pin.set_low();

    let mut start_time = SystemTime::now();
    let mut end_time = SystemTime::now();

    // Save start_time
    while echo_pin.is_low() {
        start_time = SystemTime::now();
    }

    // Save arrival_time
    while echo_pin.is_high() {
        end_time = SystemTime::now();
    }

    // Duration between start and arrival
    let elapsed_time = end_time.duration_since(start_time)?;

    // Multiply with sonic speed (34300 cm/s) and then divide by 2, as the wave hits and returns
    // back.
    let distance = (elapsed_time.as_secs_f64() * SONIC_SPEED) / 2_f64;

    Ok(distance)
}
