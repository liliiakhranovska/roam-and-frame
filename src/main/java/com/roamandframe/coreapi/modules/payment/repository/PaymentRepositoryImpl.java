package com.roamandframe.coreapi.modules.payment.repository;

import com.roamandframe.coreapi.modules.payment.model.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;

    PaymentRepositoryImpl(PaymentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Payment create(UUID orderId, UUID customerId, BigDecimal amount, String reference) {
        PaymentJpaEntity e = jpaRepository.saveAndFlush(new PaymentJpaEntity(orderId, customerId, amount, reference));
        return new Payment(e.getId(), e.getOrderId(), e.getCustomerId(), e.getAmount(), e.getStatus(), e.getReference());
    }
}
