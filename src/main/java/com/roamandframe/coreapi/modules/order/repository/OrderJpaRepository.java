package com.roamandframe.coreapi.modules.order.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID> {

    @EntityGraph(attributePaths = "items")
    List<OrderJpaEntity> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    @EntityGraph(attributePaths = "items")
    Optional<OrderJpaEntity> findByIdAndCustomerId(UUID id, UUID customerId);
}
