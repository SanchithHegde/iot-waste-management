import pathlib
from dataclasses import dataclass
from typing import Optional, Union

PathType = Union[str, pathlib.Path]


# class Ssl(TypedDict):
#     ca_certs: PathType
#     client_certs: PathType
#     client_keys: PathType


# class Mqtt(TypedDict):
#     broker_url: str
#     topic: str
#     qos: int
#     ssl: Optional[Ssl]


# class Config(TypedDict):
#     mqtt: Mqtt


@dataclass
class Ssl:
    ca_certs: PathType
    client_certs: PathType
    client_key: PathType


@dataclass
class Mqtt:
    broker_url: str
    topic: str
    qos: int
    ssl: Optional[Ssl]


@dataclass
class Config:
    mqtt: Mqtt
