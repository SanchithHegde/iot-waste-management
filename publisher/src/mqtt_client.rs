use std::{sync::Arc, time::Duration};

use anyhow::{Context, Result};
use log::{debug, info, trace};
use paho_mqtt as mqtt;

pub(crate) async fn publish_message(
    message: String,
    config: Arc<crate::config::Config>,
) -> Result<()> {
    // Build async MQTT client
    let client = mqtt::CreateOptionsBuilder::new()
        .server_uri(&config.mqtt.broker_url)
        .client_id(&config.machine.id)
        .max_buffered_messages(config.mqtt.buffer_size as i32)
        .mqtt_version(mqtt::MQTT_VERSION_5)
        .delete_oldest_messages(true)
        .create_client()
        .with_context(|| "Failed to create MQTT client")?;

    // Build connect options
    let mut connect_opts_builder = mqtt::ConnectOptionsBuilder::new();
    connect_opts_builder
        .keep_alive_interval(Duration::from_secs(config.mqtt.keep_alive_interval))
        .connect_timeout(Duration::from_secs(config.mqtt.connect_timeout))
        .automatic_reconnect(
            Duration::from_secs(config.mqtt.min_retry_interval),
            Duration::from_secs(config.mqtt.max_retry_interval),
        );

    // Include SSL options in connect options if available in config
    if let Some(ssl) = &config.mqtt.ssl {
        // Performing check for the files' existence manually because paho-mqtt doesn't check and
        // passes filenames directly to the C library during connection

        // Check if CA certificates file can be opened
        std::fs::File::open(&ssl.ca_certs).with_context(|| {
            format!(
                r#"Failed to read CA certificates file: "{}""#,
                &ssl.ca_certs.display()
            )
        })?;

        // Check if client certificates file can be opened
        std::fs::File::open(&ssl.client_certs).with_context(|| {
            format!(
                r#"Failed to read client certificates file: "{}""#,
                &ssl.client_certs.display()
            )
        })?;

        // Check if client private key file can be opened
        std::fs::File::open(&ssl.client_key).with_context(|| {
            format!(
                r#"Failed to read client private key file: "{}""#,
                &ssl.client_key.display()
            )
        })?;

        // Build SSL options
        let ssl_opts = mqtt::SslOptionsBuilder::new()
            .trust_store(&ssl.ca_certs)
            .with_context(|| "Failed to set CA certificates option")?
            .key_store(&ssl.client_certs)
            .with_context(|| "Failed to set client certificates option")?
            .private_key(&ssl.client_key)
            .with_context(|| "Failed to set client private key option")?
            .enable_server_cert_auth(true)
            .verify(true)
            .finalize();

        connect_opts_builder.ssl_options(ssl_opts);
    };

    let connect_opts = connect_opts_builder.finalize();

    // Build message
    let mqtt_message = mqtt::MessageBuilder::new()
        .topic(&config.mqtt.topic)
        .payload(message.as_bytes())
        .qos(config.mqtt.qos as i32)
        .retained(true)
        .finalize();

    let handle: tokio::task::JoinHandle<Result<()>> = tokio::task::spawn(async move {
        // Connect to client using connect options
        client.connect(connect_opts).await.with_context(|| {
            format!(
                r#"Failed to establish connection with MQTT broker with address "{}""#,
                &config.mqtt.broker_url
            )
        })?;
        trace!("Connected to MQTT broker");

        // Publish message under topic
        client.publish(mqtt_message).await.with_context(|| {
            format!(
                r#"Failed to publish message "{}" under topic "{}""#,
                &message, config.mqtt.topic
            )
        })?;
        debug!(
            r#"Published message "{}" under topic "{}""#,
            &message, config.mqtt.topic
        );

        // Disconnect from broker
        client.disconnect(None).await.with_context(|| {
            format!(
                r#"Failed to disconnect from MQTT broker with address "{}""#,
                &config.mqtt.broker_url
            )
        })?;
        trace!("Disconnected from MQTT broker");

        Ok(())
    });

    (handle.await?)?;
    info!("Successfully sent message to MQTT broker");

    Ok(())
}
