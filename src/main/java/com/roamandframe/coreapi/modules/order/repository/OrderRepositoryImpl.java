package com.roamandframe.coreapi.modules.order.repository;

import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    OrderRepositoryImpl(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Order create(UUID id, UUID customerId, List<OrderItem> items, BigDecimal total) {
        return jpaRepository.saveAndFlush(new OrderJpaEntity(id, customerId, total, items)).toModel();
    }

    @Override
    public List<Order> findByCustomerId(UUID customerId) {
        return jpaRepository.findByCustomerIdOrderByCreatedAtDesc(customerId).stream()
                .map(OrderJpaEntity::toModel)
                .toList();
    }

    @Override
    public Optional<Order> findByIdAndCustomerId(UUID id, UUID customerId) {
        return jpaRepository.findByIdAndCustomerId(id, customerId).map(OrderJpaEntity::toModel);
    }
}
