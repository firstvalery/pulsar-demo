package org.firstvalery.pulsardemo.pulsar.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.shade.com.google.gson.Gson;
import org.firstvalery.pulsardemo.pulsar.Dto;
import org.firstvalery.pulsardemo.pulsar.PulsarClientProvider;
import org.firstvalery.pulsardemo.pulsar.config.PulsarConfig;
import org.firstvalery.pulsardemo.pulsar.scheduler.PulsarScheduler;
import org.firstvalery.pulsardemo.pulsar.scheduler.PulsarServiceException;
import org.springframework.boot.autoconfigure.integration.IntegrationProperties;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TopicHandler implements NotificationHandler {
    private final PulsarConfig pulsarConfig;
    private final PulsarClientProvider pulsarClientProvider;
    private final Gson gson = new Gson();
    Random random = new Random();


    public TopicHandler(PulsarConfig pulsarConfig,
                        PulsarClientProvider pulsarClientProvider) {
        this.pulsarConfig = pulsarConfig;
        this.pulsarClientProvider = pulsarClientProvider;
    }

    @Override
    public String getTopic() {
        return pulsarConfig.getTopic();
    }

    @Override
    public ConsumerBuilder<byte[]> prepareConsumer() {
        return pulsarClientProvider.getClient()
                .newConsumer()
                .topic(pulsarConfig.getTopic())
                .subscriptionName(pulsarConfig.getTopic())
                .subscriptionType(SubscriptionType.Shared)
                .receiverQueueSize(pulsarConfig.getDefaultTopicSize())
                .negativeAckRedeliveryDelay(pulsarConfig.getNegativeAckRedeliveryDelay(), TimeUnit.SECONDS);
    }

    @Override
    public void handle(Message<byte[]> message) {
        if (random.nextBoolean())
            throw new PulsarServiceException("some exception occurred!!!");
        Dto dto = extract(message);
        log.info("received dto: " + dto);
    }

    private Dto extract(Message<byte[]> message) {
        return gson.fromJson(toJson(message), Dto.class);
    }

    private String toJson(Message<byte[]> message) {
        return new String(message.getValue(), StandardCharsets.UTF_8);
    }

}
