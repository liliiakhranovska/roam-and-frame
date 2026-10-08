package com.roamandframe.coreapi.modules.payment.service;

import com.roamandframe.coreapi.modules.payment.gateway.PaymentGateway;
import com.roamandframe.coreapi.modules.payment.model.Payment;
import com.roamandframe.coreapi.modules.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentRepository paymentRepository, PaymentGateway paymentGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public Payment authorize(UUID orderId, UUID customerId, BigDecimal amount) {
        String reference = paymentGateway.authorize(orderId, amount);
        try {
            return paymentRepository.create(orderId, customerId, amount, reference);
        } catch (RuntimeException e) {
            paymentGateway.voidAuthorization(reference);
            throw e;
        }
    }

    /** Cancels an authorization at the provider; the caller's transaction rollback removes our own record. */
    public void voidAuthorization(Payment payment) {
        paymentGateway.voidAuthorization(payment.reference());
    }
}
