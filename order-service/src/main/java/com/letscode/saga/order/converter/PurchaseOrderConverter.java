package com.letscode.saga.order.converter;

import com.letscode.saga.commons.dto.OrderRequestDto;
import com.letscode.saga.commons.event.OrderStatus;
import com.letscode.saga.order.entity.PurchaseOrder;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderConverter implements Converter<OrderRequestDto, PurchaseOrder> {
    @Override
    public PurchaseOrder convert(OrderRequestDto dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());

        return purchaseOrder;
    }
}
