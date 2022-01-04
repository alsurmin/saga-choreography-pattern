package com.letscode.saga.payment.service;

import com.letscode.saga.commons.dto.OrderRequestDto;
import com.letscode.saga.commons.dto.PaymentRequestDto;
import com.letscode.saga.commons.event.OrderEvent;
import com.letscode.saga.commons.event.PaymentEvent;
import com.letscode.saga.commons.event.PaymentStatus;
import com.letscode.saga.payment.entity.UserBalance;
import com.letscode.saga.payment.entity.UserTransaction;
import com.letscode.saga.payment.repository.UserBalanceRepo;
import com.letscode.saga.payment.repository.UserTransactionRepo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@ComponentScan(basePackages = "com.letscode.saga.payment")
public class PaymentService {

    private final UserBalanceRepo userBalanceRepo;
    private final UserTransactionRepo userTransactionRepo;

    public PaymentService(UserBalanceRepo userBalanceRepo, UserTransactionRepo userTransactionRepo) {
        this.userBalanceRepo = userBalanceRepo;
        this.userTransactionRepo = userTransactionRepo;
    }

    @PostConstruct
    public void initUserBalanceInDB() {
        userBalanceRepo.saveAll(Arrays.asList(
                new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 999)
        ));
    }

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDto orderRequestDto = orderEvent.getOrderRequestDto();
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(
                orderRequestDto.getOrderId(),
                orderRequestDto.getUserId(),
                orderRequestDto.getAmount());

        return userBalanceRepo.findById(orderRequestDto.getUserId())
                .filter(ub -> ub.getPrice() > orderRequestDto.getAmount())
                .map(ub -> {
                    ub.setPrice(ub.getPrice() - orderRequestDto.getAmount());

                    UserTransaction userTransaction = new UserTransaction(
                            orderRequestDto.getOrderId(),
                            orderRequestDto.getUserId(),
                            orderRequestDto.getAmount());

                    userTransactionRepo.save(userTransaction);
                    return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        Integer orderId = orderEvent.getOrderRequestDto().getOrderId();
        Integer userId = orderEvent.getOrderRequestDto().getUserId();
        userTransactionRepo.findById(orderId)
                .ifPresent(ut -> {
                        userTransactionRepo.delete(ut);
                        userTransactionRepo.findById(userId).ifPresent(ub -> {
                            ub.setAmount(ub.getAmount()+ut.getAmount());
                        });
                });
    }
}
