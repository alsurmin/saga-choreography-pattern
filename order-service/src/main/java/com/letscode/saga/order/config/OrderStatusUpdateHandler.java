package com.letscode.saga.order.config;

import com.letscode.saga.commons.dto.OrderRequestDto;
import com.letscode.saga.commons.event.OrderStatus;
import com.letscode.saga.commons.event.PaymentStatus;
import com.letscode.saga.order.entity.PurchaseOrder;
import com.letscode.saga.order.repository.OrderRepo;
import com.letscode.saga.order.service.OrderStatusPublisher;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {

    private final OrderRepo orderRepo;
    private final OrderStatusPublisher publisher;

    public OrderStatusUpdateHandler(OrderRepo orderRepo, OrderStatusPublisher publisher) {
        this.orderRepo = orderRepo;
        this.publisher = publisher;
    }

    @Transactional
    public void updateOrder(int id, Consumer<PurchaseOrder> consumer) {
        orderRepo.findById(id).ifPresent(consumer.andThen(this::updateOrder));
    }

    private void updateOrder(PurchaseOrder purchaseOrder) {
        boolean isPaymentCompleted = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isPaymentCompleted ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        purchaseOrder.setOrderStatus(orderStatus);
//        if (!isPaymentCompleted) {
//            publisher.publishOrderEvent(convertEntityToDto(purchaseOrder), orderStatus);
//        }
    }

    private OrderRequestDto convertEntityToDto(PurchaseOrder purchaseOrder) {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setOrderId(purchaseOrder.getId());
        orderRequestDto.setUserId(purchaseOrder.getUserId());
        orderRequestDto.setAmount(purchaseOrder.getPrice());
        orderRequestDto.setProductId(purchaseOrder.getProductId());
        return orderRequestDto;
    }

}
