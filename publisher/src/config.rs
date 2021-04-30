use std::path::PathBuf;

use serde_derive::Deserialize;

#[derive(Debug, Default, Deserialize)]
pub(crate) struct Config {
    pub(crate) threshold_distance: u32,
    pub(crate) delay: u64,
    pub(crate) measurement_timeout: u64,
    pub(crate) machine: Machine,
    pub(crate) mqtt: Mqtt,
}

#[derive(Debug, Default, Deserialize)]
pub(crate) struct Machine {
    pub(crate) id: String,
    pub(crate) location: String,
}

#[derive(Debug, Default, Deserialize)]
pub(crate) struct Mqtt {
    pub(crate) broker_url: String,
    pub(crate) topic: String,
    pub(crate) qos: u32,
    pub(crate) buffer_size: u32,
    pub(crate) keep_alive_interval: u64,
    pub(crate) connect_timeout: u64,
    pub(crate) min_retry_interval: u64,
    pub(crate) max_retry_interval: u64,
    pub(crate) ssl: Option<Ssl>,
}

#[derive(Debug, Default, Deserialize)]
pub(crate) struct Ssl {
    pub(crate) ca_certs: PathBuf,
    pub(crate) client_certs: PathBuf,
    pub(crate) client_key: PathBuf,
}
