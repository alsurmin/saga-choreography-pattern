package com.letscode.saga.payment.repository;

import com.letscode.saga.payment.entity.UserTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTransactionRepo extends JpaRepository<UserTransaction, Integer> {
}
