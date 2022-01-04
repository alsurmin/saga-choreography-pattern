package com.letscode.saga.order.service;

import com.letscode.saga.commons.dto.OrderRequestDto;
import com.letscode.saga.commons.event.OrderStatus;
import com.letscode.saga.order.converter.PurchaseOrderConverter;
import com.letscode.saga.order.entity.PurchaseOrder;
import com.letscode.saga.order.repository.OrderRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final OrderRepo orderRepo;
    private final OrderStatusPublisher orderStatusPublisher;
    private final PurchaseOrderConverter purchaseOrderConverter;

    public OrderService(OrderRepo orderRepo, OrderStatusPublisher orderStatusPublisher, PurchaseOrderConverter purchaseOrderConverter) {
        this.orderRepo = orderRepo;
        this.orderStatusPublisher = orderStatusPublisher;
        this.purchaseOrderConverter = purchaseOrderConverter;
    }

    @Transactional
    public PurchaseOrder createOrder(OrderRequestDto orderRequestDto) {
        PurchaseOrder order = orderRepo.save(Objects.requireNonNull(purchaseOrderConverter.convert(orderRequestDto)));

        orderRequestDto.setOrderId(order.getId());
        //produce kafka event with status ORDER_CREATED
        orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
        return order;
    }

    public List<PurchaseOrder> getAllOrders() {
        return orderRepo.findAll();
    }
}