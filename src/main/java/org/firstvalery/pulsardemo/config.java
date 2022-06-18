package org.firstvalery.pulsardemo;

import org.apache.pulsar.client.api.PulsarClientException;
import org.firstvalery.pulsardemo.pulsar.PulsarClientProvider;
import org.firstvalery.pulsardemo.pulsar.config.PulsarConfig;
import org.firstvalery.pulsardemo.pulsar.consumer.NotificationHandler;
import org.firstvalery.pulsardemo.pulsar.consumer.PulsarConsumerRegister;
import org.firstvalery.pulsardemo.pulsar.consumer.TopicHandler;
import org.firstvalery.pulsardemo.pulsar.scheduler.PulsarScheduler;
import org.firstvalery.pulsardemo.pulsar.scheduler.ToTopicScheduler;
import org.firstvalery.pulsardemo.service.EventSupplier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class config {

    @Bean
    @ConfigurationProperties(prefix = "pulsar")
    PulsarConfig pulsarConfig() {
        return new PulsarConfig();
    }

    @Bean
    PulsarClientProvider pulsarClientProvider(PulsarConfig pulsarConfig) throws PulsarClientException {
        return new PulsarClientProvider(pulsarConfig);
    }

    @Bean
    PulsarScheduler scheduler(PulsarClientProvider provider, PulsarConfig config) throws PulsarClientException {
        return new ToTopicScheduler(provider, config);
    }

    @Bean
    NotificationHandler handler(PulsarConfig config, PulsarClientProvider provider) {
        return new TopicHandler(config, provider);
    }

    @Bean
    PulsarConsumerRegister pulsarConsumerRegister(List<NotificationHandler> handlers) {
        return new PulsarConsumerRegister(handlers);
    }

    @Bean
    EventSupplier eventSupplier(PulsarScheduler scheduler) {
        return new EventSupplier(scheduler);
    }

}
