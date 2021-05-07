from .. import CONFIG
from . import telegram


def notify(message: str):
    if CONFIG.notifications.telegram is not None:

        telegram.send_message(CONFIG.notifications.telegram.chat_id, message)
