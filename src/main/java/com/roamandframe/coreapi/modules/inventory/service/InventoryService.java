package com.roamandframe.coreapi.modules.inventory.service;

import com.roamandframe.coreapi.modules.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /** Quantity per SKU; a SKU with no stock record counts as 0. */
    @Transactional(readOnly = true)
    public Map<String, Integer> getQuantities(Collection<String> skus) {
        Map<String, Integer> quantities = new HashMap<>(inventoryRepository.findQuantities(skus));
        skus.forEach(sku -> quantities.putIfAbsent(sku, 0));
        return quantities;
    }
}
