package org.firstvalery.pulsardemo.pulsar.consumer;

import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.Message;

public interface NotificationHandler {
    String getTopic();

    ConsumerBuilder<byte[]> prepareConsumer();

    void handle(Message<byte[]> message);
}
