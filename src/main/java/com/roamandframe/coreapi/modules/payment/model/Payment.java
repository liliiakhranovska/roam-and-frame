package com.roamandframe.coreapi.modules.payment.model;

import java.math.BigDecimal;
import java.util.UUID;

public record Payment(UUID id, UUID orderId, UUID customerId, BigDecimal amount,
                      PaymentStatus status, String reference) {
}
