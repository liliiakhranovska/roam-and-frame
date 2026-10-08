package com.roamandframe.coreapi.modules.payment.repository;

import com.roamandframe.coreapi.modules.payment.model.Payment;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentRepository {
    Payment create(UUID orderId, UUID customerId, BigDecimal amount, String reference);
}
