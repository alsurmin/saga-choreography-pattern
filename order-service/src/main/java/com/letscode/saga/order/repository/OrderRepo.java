package com.letscode.saga.order.repository;

import com.letscode.saga.order.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<PurchaseOrder, Integer> {
}
