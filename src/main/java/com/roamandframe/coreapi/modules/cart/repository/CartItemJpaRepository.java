package com.roamandframe.coreapi.modules.cart.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface CartItemJpaRepository extends JpaRepository<CartItemJpaEntity, UUID> {
    List<CartItemJpaEntity> findByCustomerIdOrderByCreatedAt(UUID customerId);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CartItemJpaEntity c where c.customerId = :customerId order by c.createdAt")
    List<CartItemJpaEntity> findForUpdateByCustomerId(@Param("customerId") UUID customerId);

    Optional<CartItemJpaEntity> findByCustomerIdAndSku(UUID customerId, String sku);
    void deleteByCustomerIdAndSku(UUID customerId, String sku);
    void deleteByCustomerId(UUID customerId);
}
