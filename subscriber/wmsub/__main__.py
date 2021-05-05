import signal
import sys
from typing import Any

from . import LOGGER


def signal_handler(signal_number: int, _stack_frame: Any):
    signal_name = {
        # pylint:disable=no-member
        signal.Signals.SIGINT: signal.Signals.SIGINT.name,
        signal.Signals.SIGQUIT: signal.Signals.SIGQUIT.name,
        signal.Signals.SIGTERM: signal.Signals.SIGTERM.name,
    }
    LOGGER.info("Received signal %s, quitting ...", signal_name[signal_number])
    sys.exit(1)


def main():
    # Register signal handlers
    signal.signal(signal.SIGINT, signal_handler)


if __name__ == "__main__":
    main()
