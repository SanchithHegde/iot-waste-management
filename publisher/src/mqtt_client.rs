use std::{sync::Arc, time::Duration};

use anyhow::Result;
use log::{debug, info, trace};
use paho_mqtt as mqtt;

pub(crate) async fn publish_message(
    message: String,
    config: Arc<crate::config::Config>,
) -> Result<()> {
    let broker_address = &config.mqtt.broker_url;

    let client = mqtt::CreateOptionsBuilder::new()
        .server_uri(broker_address)
        .client_id(&config.machine.id)
        .max_buffered_messages(config.mqtt.buffer_size as i32)
        .mqtt_version(5)
        .delete_oldest_messages(true)
        .create_client()?;

    let connect_opts = mqtt::ConnectOptionsBuilder::new()
        .keep_alive_interval(Duration::from_secs(config.mqtt.keep_alive_interval))
        .connect_timeout(Duration::from_secs(config.mqtt.connect_timeout))
        .automatic_reconnect(
            Duration::from_secs(config.mqtt.min_retry_interval),
            Duration::from_secs(config.mqtt.max_retry_interval),
        )
        .finalize();

    let handle: tokio::task::JoinHandle<Result<(), mqtt::Error>> = tokio::task::spawn(async move {
        client.connect(connect_opts).await?;
        trace!("Connected to MQTT broker");

        let mqtt_message = mqtt::MessageBuilder::new()
            .topic(&config.mqtt.topic)
            .payload(message.as_bytes())
            .qos(config.mqtt.qos as i32)
            .retained(true)
            .finalize();

        client.publish(mqtt_message).await?;
        debug!(
            r#"Published message "{}" under topic "{}""#,
            &message, config.mqtt.topic
        );

        client.disconnect(None).await?;
        trace!("Disconnected from MQTT broker");

        Ok(())
    });

    (handle.await?)?;
    info!("Successfully sent message to MQTT broker");

    Ok(())
}
