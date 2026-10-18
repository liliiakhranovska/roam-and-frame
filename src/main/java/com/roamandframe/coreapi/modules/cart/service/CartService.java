package com.roamandframe.coreapi.modules.cart.service;

import com.roamandframe.coreapi.modules.cart.model.Cart;
import com.roamandframe.coreapi.modules.cart.model.CartItem;
import com.roamandframe.coreapi.modules.cart.model.CartLine;
import com.roamandframe.coreapi.modules.catalog.model.Product;
import com.roamandframe.coreapi.modules.catalog.model.WithStock;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {

    Cart getCart(UUID customerId);

    Cart setItemQuantity(UUID customerId, String sku, int quantity);

    Cart validateCart(UUID customerId);

    void clearCart(UUID customerId);


}
