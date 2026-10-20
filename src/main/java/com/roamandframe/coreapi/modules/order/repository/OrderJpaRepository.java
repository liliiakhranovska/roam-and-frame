package com.roamandframe.coreapi.modules.order.repository;

import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, UUID>, JpaSpecificationExecutor<OrderJpaEntity> {

    @EntityGraph(attributePaths = "items")
    List<OrderJpaEntity> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    @EntityGraph(attributePaths = "items")
    Optional<OrderJpaEntity> findByIdAndCustomerId(UUID id, UUID customerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderJpaEntity o where o.id = :id")
    Optional<OrderJpaEntity> findForUpdateById(@Param("id") UUID id);

    @EntityGraph(attributePaths = "items")
    List<OrderJpaEntity> findByIdIn(Collection<UUID> ids);
}
