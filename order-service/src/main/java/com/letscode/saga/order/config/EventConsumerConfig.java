package com.letscode.saga.order.config;

import com.letscode.saga.commons.event.PaymentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.function.Consumer;

@Configuration
@ComponentScan(basePackages = {"com.letscode.saga.order.config"})
@Import(OrderStatusUpdateHandler.class)
public class EventConsumerConfig {

    private final OrderStatusUpdateHandler handler;

    public EventConsumerConfig(OrderStatusUpdateHandler handler) {
        this.handler = handler;
    }

    @Bean(name = "paymentEventConsumer")
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return (payment -> handler.updateOrder(payment.getPaymentRequestDto().getOrderId(), purchaseOrder -> {
            purchaseOrder.setPaymentStatus(payment.getPaymentStatus());
        }));
    }
}
