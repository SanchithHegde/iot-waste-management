import logging

import coloredlogs
import dacite
import toml

from iotwm.config import Config

# Enable logging
LOGGER = logging.getLogger(__name__)
coloredlogs.install(
    level="DEBUG",
    logger=LOGGER,
    fmt="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
)

# Load config
config: Config = dacite.from_dict(data_class=Config, data=toml.load("config.toml"))
LOGGER.info(config)
