package com.letscode.saga.payment.config;

import com.letscode.saga.commons.event.OrderEvent;
import com.letscode.saga.commons.event.OrderStatus;
import com.letscode.saga.commons.event.PaymentEvent;
import com.letscode.saga.payment.service.PaymentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class PaymentConsumerConfig {
    private final PaymentService paymentService;

    public PaymentConsumerConfig(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> paymentProcessor() {
        return orderEventFlux -> orderEventFlux.flatMap(this::processPayment);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {
        if (OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
            return Mono.fromSupplier(() -> paymentService.newOrderEvent(orderEvent));
        } else {
            return Mono.fromRunnable(() -> paymentService.cancelOrderEvent(orderEvent));
        }
    }
}
