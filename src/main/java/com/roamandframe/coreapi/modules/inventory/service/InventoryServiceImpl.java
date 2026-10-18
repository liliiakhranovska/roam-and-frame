package com.roamandframe.coreapi.modules.inventory.service;

import com.roamandframe.coreapi.modules.inventory.exception.StockReservationException;
import com.roamandframe.coreapi.modules.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Map<String, Integer> getQuantities(Collection<String> skus) {
        Map<String, Integer> quantities = new HashMap<>(inventoryRepository.findQuantities(skus));
        skus.forEach(sku -> quantities.putIfAbsent(sku, 0));
        return quantities;
    }

    @Transactional
    @Override
    public void reserve(Map<String, Integer> quantities) {
        // fixed SKU order, so concurrent reservations lock rows in the same order
        new TreeMap<>(quantities).forEach((sku, quantity) -> {
            if (!inventoryRepository.reserve(sku, quantity)) {
                throw new StockReservationException(sku, quantity);
            }
        });
    }
}
