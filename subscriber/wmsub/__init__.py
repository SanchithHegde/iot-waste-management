import logging

import coloredlogs
import dacite
import toml
from platformdirs import PlatformDirs

from .config import Config

# Enable logging
LOGGER = logging.getLogger(__name__)
LOG_LEVEL = "INFO"
LOG_FORMAT = "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
coloredlogs.install(
    level=LOG_LEVEL,
    logger=LOGGER,
    fmt=LOG_FORMAT,
)

# Load config
dirs = PlatformDirs(appname="iot-waste-management")
CONFIG: Config = dacite.from_dict(
    data_class=Config, data=toml.load(f"{dirs.user_config_dir}/config.toml")
)
LOGGER.debug(CONFIG)
