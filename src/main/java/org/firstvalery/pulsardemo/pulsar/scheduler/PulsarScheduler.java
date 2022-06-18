package org.firstvalery.pulsardemo.pulsar.scheduler;


public interface PulsarScheduler {
    <T> void send(T body);
}
