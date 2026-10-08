package com.roamandframe.coreapi.modules.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

interface ShipmentJpaRepository extends JpaRepository<ShipmentJpaEntity, UUID> {
    List<ShipmentJpaEntity> findByOrderIdIn(Collection<UUID> orderIds);
}
