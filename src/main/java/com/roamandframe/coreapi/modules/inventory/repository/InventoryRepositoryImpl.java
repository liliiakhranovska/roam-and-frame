package com.roamandframe.coreapi.modules.inventory.repository;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class InventoryRepositoryImpl implements InventoryRepository {

    private final StockItemJpaRepository jpaRepository;

    InventoryRepositoryImpl(StockItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Map<String, Integer> findQuantities(Collection<String> skus) {
        return jpaRepository.findAllById(skus).stream()
                .collect(Collectors.toMap(StockItemJpaEntity::getSku, StockItemJpaEntity::getQuantity));
    }
}
