package org.firstvalery.pulsardemo.pulsar.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PulsarConfig {
    private String serviceUrl;
    private String topic;
    private int defaultTopicSize;
    private int negativeAckRedeliveryDelay;
    private int ackTimeout;
}
