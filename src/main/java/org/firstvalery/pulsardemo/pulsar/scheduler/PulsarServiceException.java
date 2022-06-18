package org.firstvalery.pulsardemo.pulsar.scheduler;

import java.util.Objects;

public class PulsarServiceException extends RuntimeException {
    public PulsarServiceException() {
    }

    public PulsarServiceException(String message) {
        super(message);
    }

    public PulsarServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PulsarServiceException(Throwable cause) {
        super(cause);
    }

    Throwable unwrap() {
        Throwable cause = getCause();
        if (Objects.isNull(cause))
            return this;
        return cause;
    }
}
