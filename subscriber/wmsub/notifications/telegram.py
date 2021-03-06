import logging

import coloredlogs
from telegram.ext import Updater

from .. import CONFIG, LOG_FORMAT, LOG_LEVEL

LOGGER = logging.getLogger(__name__)

coloredlogs.install(
    level=LOG_LEVEL,
    logger=LOGGER,
    fmt=LOG_FORMAT,
)

updater = Updater(
    token=CONFIG.notifications.telegram.bot_token,
    workers=CONFIG.notifications.telegram.workers,
)


def send_message(chat_id: str, message: str):
    updater.bot.send_message(chat_id=chat_id, text=message)

    LOGGER.debug("Sent a message on Telegram successfully")
