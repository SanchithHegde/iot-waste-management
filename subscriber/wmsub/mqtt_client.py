from typing import Any, Dict, Optional

from paho.mqtt.client import (
    MQTT_CLEAN_START_FIRST_ONLY,
    Client,
    MQTTMessage,
    MQTTv5,
    SubscribeOptions,
)
from paho.mqtt.packettypes import PacketTypes
from paho.mqtt.properties import Properties
from paho.mqtt.reasoncodes import ReasonCodes

from . import LOGGER
from .config import Config
from .notifications import notify
from .utils import parse_topic_location_machine_id


class MqttClient:  # pylint:disable=too-few-public-methods
    def __init__(self, config: Config):
        # Build client
        self._client = Client(client_id=config.mqtt.machine_id, protocol=MQTTv5)

        # Set callback methods
        self._client.on_connect = self._on_connect
        self._client.on_disconnect = self._on_disconnect
        self._client.on_message = self._on_message
        self._client.on_subscribe = self._on_subscribe

        # Set other attributes
        self._host = config.mqtt.broker_address
        self._port = config.mqtt.port
        self._topic = config.mqtt.topic_prefix
        self._keepalive = config.mqtt.keepalive

        # Set SSL options if they're present in config
        if config.mqtt.ssl is not None or not config.mqtt.ssl:
            self._client.tls_set(
                ca_certs=config.mqtt.ssl.ca_certs,
                certfile=config.mqtt.ssl.client_certs,
                keyfile=config.mqtt.ssl.client_key,
            )

    def _on_connect(
        self,
        _client: Client,
        _user_data: Any,
        _flags: Dict[str, int],
        reason_code: int,
        _properties: Optional[Properties],
    ):
        if reason_code == 0:
            LOGGER.debug("Connected to MQTT broker")

        else:
            LOGGER.error(
                'Failed to establish connection with MQTT broker with address "%s:%s"',
                self._host,
                self._port,
            )
            raise Exception(
                ReasonCodes(
                    packetType=PacketTypes.CONNACK, identifier=reason_code
                ).getName()
            )

    @staticmethod
    def _on_disconnect(_client: Client, _user_data: Any, reason_code: int):
        if reason_code == 0:
            LOGGER.debug("Disconnected from MQTT broker")

        else:
            LOGGER.error("Unexpected disconnect")
            raise Exception(
                ReasonCodes(
                    packetType=PacketTypes.DISCONNECT, identifier=reason_code
                ).getName()
            )

    @staticmethod
    def _on_message(_client: Client, _userdata: Any, message: MQTTMessage):
        payload = message.payload.decode("UTF-8")
        LOGGER.info("Received message from broker:\n%s", payload)
        topic_prefix, location, _machine_id = parse_topic_location_machine_id(
            message.topic
        )
        title = "Take me out!"
        msg = f"Dustbin at {location} is full and needs your attention!"
        notify(title, msg, topic_prefix, location)

    def _on_subscribe(
        self,
        _client: Client,
        _user_data: Any,
        _mid: int,
        _granted_qos: int,
        _properties: Optional[Properties],
    ):
        LOGGER.debug('Subscribed to topic "%s"', self._topic)

    def receive_messages(self):
        # Connect asynchronously to broker
        self._client.connect(
            host=self._host,
            port=self._port,
            keepalive=self._keepalive,
            clean_start=MQTT_CLEAN_START_FIRST_ONLY,
        )

        # Subscribe to messages
        self._client.subscribe(topic=self._topic, options=SubscribeOptions(qos=2))

        # Start the network loop
        LOGGER.info("Starting loop")
        self._client.loop_forever()
