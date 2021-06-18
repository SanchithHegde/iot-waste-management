import logging

import coloredlogs
from requests_futures.sessions import FuturesSession

from .. import CONFIG, LOG_FORMAT, LOG_LEVEL

CREATE_NOTIFICATION_URL = "https://onesignal.com/api/v1/notifications"

LOGGER = logging.getLogger(__name__)

coloredlogs.install(
    level=LOG_LEVEL,
    logger=LOGGER,
    fmt=LOG_FORMAT,
)


def push_notification(title: str, message: str, location: str):
    # Build headers and JSON data for POST request
    headers = {
        "Content-Type": "application/json; charset=utf-8",
        "Authorization": f"Basic {CONFIG.notifications.onesignal.api_key}",
    }
    filters = [{"field": "tag", "key": "location", "relation": "=", "value": location}]
    app_id = CONFIG.notifications.onesignal.app_id
    contents = {"en": message}
    headings = {"en": title}
    is_android = "true"
    data = {
        "filters": filters,
        "app_id": app_id,
        "contents": contents,
        "headings": headings,
        "isAndroid": is_android,
    }

    # Send POST request
    session = FuturesSession()
    _response = session.post(
        CREATE_NOTIFICATION_URL,
        headers=headers,
        json=data,
    ).result()

    session.close()

    LOGGER.debug("Pushed a notification to OneSignal successfully")
