from typing import Tuple


def parse_topic_location_machine_id(string: str) -> Tuple[str, str, str]:
    parts = string.split(sep="/")

    if len(parts) != 3:
        raise ValueError(f"Expected 3 parts in topic string, found {len(parts)}")

    [topic_prefix, location, machine_id] = parts
    return (topic_prefix, location, machine_id)
