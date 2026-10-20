package com.roamandframe.coreapi.modules.order.repository;

import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderFilter;
import com.roamandframe.coreapi.modules.order.model.OrderItem;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private static final int MAX_RESULTS = 500;

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

    @Override
    public Optional<Order> markShipped(UUID id) {
        return jpaRepository.findForUpdateById(id).map(entity -> {
            entity.ship();
            return entity.toModel();
        });
    }

    @Override
    public List<Order> search(OrderFilter filter) {
        PageRequest newestFirst = PageRequest.of(0, MAX_RESULTS,
                Sort.by(Sort.Direction.DESC, "createdAt").and(Sort.by("id")));

        List<UUID> ids = jpaRepository.findAll(OrderSpecifications.matching(filter), newestFirst)
                .getContent().stream()
                .map(OrderJpaEntity::getId)
                .toList();
        if (ids.isEmpty()) {
            return List.of();
        }

        Map<UUID, OrderJpaEntity> byId = jpaRepository.findByIdIn(ids).stream()
                .collect(Collectors.toMap(OrderJpaEntity::getId, Function.identity()));
        return ids.stream().map(byId::get).map(OrderJpaEntity::toModel).toList();
    }
}
