package com.letscode.saga.order.config;

import com.letscode.saga.commons.event.OrderEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class OrderPublisherConfig {

    @Bean
    public Sinks.Many<OrderEvent> orderEvent() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean(name = "orderSupplier")
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sinks) {
        return sinks::asFlux;
    }

}
