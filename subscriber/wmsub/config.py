import pathlib
from dataclasses import dataclass
from typing import Optional, Union

PathType = Union[str, pathlib.Path]


@dataclass
class Ssl:
    ca_certs: PathType
    client_certs: PathType
    client_key: PathType


@dataclass
class Mqtt:
    broker_address: str
    port: int
    topic_prefix: str
    machine_id: str
    keepalive: int
    ssl: Optional[Ssl]


@dataclass
class Telegram:
    bot_token: str
    chat_id: str
    workers: int


@dataclass
class OneSignal:
    app_id: str
    api_key: str


@dataclass
class Notifications:
    telegram: Optional[Telegram]
    onesignal: Optional[OneSignal]


@dataclass
class Config:
    mqtt: Mqtt
    notifications: Notifications
