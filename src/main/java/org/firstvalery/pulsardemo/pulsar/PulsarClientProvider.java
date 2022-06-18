package org.firstvalery.pulsardemo.pulsar;


import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.firstvalery.pulsardemo.pulsar.config.PulsarConfig;

import javax.annotation.PreDestroy;
import java.util.Objects;

@Slf4j
public class PulsarClientProvider {
    private final PulsarClient pulsarClient;

    public PulsarClientProvider(PulsarConfig pulsarConfig) throws PulsarClientException {
        this.pulsarClient = PulsarClient.builder().serviceUrl(Objects.requireNonNull(pulsarConfig.getServiceUrl())).build();
        log.info("Pulsar client was created");
    }

    public PulsarClient getClient() {
        return this.pulsarClient;
    }


    @PreDestroy
    protected void stopClient() {
        pulsarClient.closeAsync().thenRun(() -> log.info("Pulsar client was stopped"));
    }
}
