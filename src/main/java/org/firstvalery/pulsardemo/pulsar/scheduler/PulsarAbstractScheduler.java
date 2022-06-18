package org.firstvalery.pulsardemo.pulsar.scheduler;


import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.shade.com.google.gson.Gson;
import org.firstvalery.pulsardemo.pulsar.PulsarClientProvider;
import org.firstvalery.pulsardemo.pulsar.config.PulsarConfig;

import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;


@Slf4j
public abstract class PulsarAbstractScheduler implements PulsarScheduler {
    protected final PulsarClientProvider pulsarClientProvider;
    protected Producer<byte[]> producer;
    protected final PulsarConfig pulsarConfig;
    private final Gson gson;

    protected PulsarAbstractScheduler(PulsarClientProvider pulsarClientProvider, PulsarConfig pulsarConfig, Gson gson) throws PulsarClientException {
        this.pulsarClientProvider = pulsarClientProvider;
        this.pulsarConfig = pulsarConfig;
        this.gson = gson;
        createProducer();
    }

    abstract void createProducer() throws PulsarClientException;

    public <T> void send(T body) {
        producer.newMessage()
                .value(createMessage(body))
                .sendAsync()
                .handle((messageId, error) -> {
                    if (error != null) {
                        log.error("error during sending to queue {}", producer.getTopic(), error);
                        throw new PulsarServiceException(error);
                    }
                    log.debug("Message ID={} was put to queue {}.", messageId, producer.getTopic());
                    return messageId;
                });
    }


    protected <T> byte[] createMessage(T body) {
        return gson.toJson(body).getBytes(StandardCharsets.UTF_8);
    }

    @PreDestroy
    private void stopProducer() {
        producer.closeAsync().thenRun(() -> log.info("Producer of queue {} was closed.", producer.getTopic()));
    }
}
