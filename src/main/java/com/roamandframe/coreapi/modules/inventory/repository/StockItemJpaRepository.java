package com.roamandframe.coreapi.modules.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

interface StockItemJpaRepository extends JpaRepository<StockItemJpaEntity, String> {
}
