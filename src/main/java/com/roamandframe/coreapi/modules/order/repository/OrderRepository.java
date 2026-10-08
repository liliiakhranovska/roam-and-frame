package com.roamandframe.coreapi.modules.order.repository;

import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order create(UUID id, UUID customerId, List<OrderItem> items, BigDecimal total);
    List<Order> findByCustomerId(UUID customerId);
    Optional<Order> findByIdAndCustomerId(UUID id, UUID customerId);
}
