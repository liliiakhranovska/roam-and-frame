package com.roamandframe.coreapi.modules.inventory.repository;

import java.util.Collection;
import java.util.Map;

public interface InventoryRepository {
    Map<String, Integer> findQuantities(Collection<String> skus);
}
