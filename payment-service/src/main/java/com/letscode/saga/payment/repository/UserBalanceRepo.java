package com.letscode.saga.payment.repository;

import com.letscode.saga.payment.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepo extends JpaRepository<UserBalance, Integer> {
}
