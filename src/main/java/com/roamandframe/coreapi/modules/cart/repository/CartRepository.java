package com.roamandframe.coreapi.modules.cart.repository;

import com.roamandframe.coreapi.modules.cart.model.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartRepository {
    List<CartItem> findItems(UUID customerId);
    List<CartItem> findItemsForUpdate(UUID customerId);
    void saveItem(UUID customerId, String sku, int quantity);
    void deleteItem(UUID customerId, String sku);
    void deleteAll(UUID customerId);
}
