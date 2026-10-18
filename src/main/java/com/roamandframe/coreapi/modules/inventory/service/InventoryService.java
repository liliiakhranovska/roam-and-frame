package com.roamandframe.coreapi.modules.inventory.service;

import java.util.Collection;
import java.util.Map;

public interface InventoryService {

    Map<String, Integer> getQuantities(Collection<String> skus);

    void reserve(Map<String, Integer> quantities);
}
