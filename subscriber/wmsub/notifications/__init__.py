from .. import CONFIG
from . import onesignal, telegram


def notify(title: str, message: str, _topic_prefix: str, location: str):
    if CONFIG.notifications.telegram is not None:

        telegram.send_message(CONFIG.notifications.telegram.chat_id, message)

    if CONFIG.notifications.onesignal is not None:
        onesignal.push_notification(title, message, location)
