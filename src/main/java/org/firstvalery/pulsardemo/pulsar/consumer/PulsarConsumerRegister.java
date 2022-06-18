package org.firstvalery.pulsardemo.pulsar.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class PulsarConsumerRegister {
    private final List<Consumer<byte[]>> consumers = new ArrayList<>();

    public PulsarConsumerRegister(List<NotificationHandler> handlers) {
        handlers.forEach(this::createConsumer);
    }

    private void createConsumer(NotificationHandler handler) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        handler.prepareConsumer()
                .subscribeAsync()
                .thenAcceptAsync(consumer -> listenOnTopic(consumer, handler), executorService)
                .exceptionally(e -> {
                    log.error("Error occurs! Related topic is {}.", handler.getTopic(), e);
                    return null;
                });
    }

    private void listenOnTopic(Consumer<byte[]> consumer, NotificationHandler handler) {
        log.info("Consumer was created for queue{}.", consumer.getTopic());
        consumers.add(consumer);
        do {
            try {
                Message<byte[]> message = consumer.receive();
                log.debug("Received message from the queue {} ID={}", consumer.getTopic());
                handleMessage(message, handler, consumer);
            } catch (Exception e) {
                log.error("error occurred during listening the queue {}!", consumer.getTopic(), e);
            }
        } while (true);
    }

    private void handleMessage(Message<byte[]> message, NotificationHandler handler, Consumer<byte[]> consumer) {
        try {
            handler.handle(message);
            consumer.acknowledge(message);
            log.debug("Message ID={} from topic = {} was acked.", message.getMessageId(), consumer.getTopic());
        } catch (Exception e) {
            log.error("Error occurred during handling message ID={} from the queue = {}!", message.getMessageId(), consumer.getTopic(), e);
            consumer.negativeAcknowledge(message);
            log.warn("Sending to retry message ID={} from the queue = {}!", message.getMessageId(), consumer.getTopic());
        }
    }


    @PreDestroy
    void stop() {
        consumers.forEach(consumer ->
                consumer.closeAsync()
                        .thenRun(() -> log.info("Consumer of the queue {} was closed.", consumer.getTopic())));
    }
}
