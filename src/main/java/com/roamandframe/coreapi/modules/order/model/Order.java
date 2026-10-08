package com.roamandframe.coreapi.modules.order.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record Order(UUID id, OrderStatus status, BigDecimal total, Instant createdAt, List<OrderItem> items) {
}
