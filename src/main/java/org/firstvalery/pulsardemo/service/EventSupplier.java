package org.firstvalery.pulsardemo.service;

import org.firstvalery.pulsardemo.pulsar.Dto;
import org.firstvalery.pulsardemo.pulsar.scheduler.PulsarScheduler;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EventSupplier {
    private final PulsarScheduler scheduler;

    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public EventSupplier(PulsarScheduler scheduler) {
        this.scheduler = scheduler;
    }

    @PostConstruct
    void init() {
        executorService.scheduleAtFixedRate(this::newEvent, 1, 60, TimeUnit.SECONDS);
    }

    void newEvent() {
        for (int i = 0; i < 100; i++) {
            Dto dto = new Dto(Instant.now(), "time");
            scheduler.send(dto);
        }
    }
}
