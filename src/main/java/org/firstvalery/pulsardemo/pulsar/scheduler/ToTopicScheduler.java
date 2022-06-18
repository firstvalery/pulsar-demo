package org.firstvalery.pulsardemo.pulsar.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.shade.com.google.gson.Gson;
import org.firstvalery.pulsardemo.pulsar.PulsarClientProvider;
import org.firstvalery.pulsardemo.pulsar.config.PulsarConfig;

import java.util.Objects;

@Slf4j
public class ToTopicScheduler extends PulsarAbstractScheduler {

    public ToTopicScheduler(PulsarClientProvider pulsarClientProvider,
                            PulsarConfig pulsarConfig) throws PulsarClientException {
        super(pulsarClientProvider, pulsarConfig, new Gson());
    }

    @Override
    void createProducer() throws PulsarClientException {
        this.producer = pulsarClientProvider.getClient()
                .newProducer(Schema.BYTES)
                .enableBatching(false)
                .topic(Objects.requireNonNull(pulsarConfig.getTopic(), "название очереди не м.б. null"))
                .producerName(pulsarConfig.getTopic())
                .create();
        log.info("Producer очереди {} был создан", producer.getTopic());
    }
}
