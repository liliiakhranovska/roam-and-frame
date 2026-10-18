package com.roamandframe.coreapi.modules.payment.service;

import com.roamandframe.coreapi.modules.payment.model.Payment;
import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    Payment authorize(UUID orderId, UUID customerId, BigDecimal amount);

    void voidAuthorization(Payment payment);
}
