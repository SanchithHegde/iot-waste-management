use serde_derive::Deserialize;

#[derive(Debug, Default, Deserialize)]
pub(crate) struct Config {
    pub(crate) threshold_distance: u32,
    pub(crate) delay: u64,
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
}
