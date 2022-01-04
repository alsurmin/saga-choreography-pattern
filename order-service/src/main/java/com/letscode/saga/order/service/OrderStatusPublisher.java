package com.letscode.saga.order.service;

import com.letscode.saga.commons.dto.OrderRequestDto;
import com.letscode.saga.commons.event.OrderEvent;
import com.letscode.saga.commons.event.OrderStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {
    private final Sinks.Many<OrderEvent> orderSinks;

    public OrderStatusPublisher(Sinks.Many<OrderEvent> orderSinks) {
        this.orderSinks = orderSinks;
    }

    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus){
        OrderEvent orderEvent=new OrderEvent(orderRequestDto, orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}
