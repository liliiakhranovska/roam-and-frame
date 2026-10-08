package com.roamandframe.coreapi.modules.cart.service;

import com.roamandframe.coreapi.modules.cart.exception.InsufficientStockException;
import com.roamandframe.coreapi.modules.cart.model.Cart;
import com.roamandframe.coreapi.modules.cart.model.CartItem;
import com.roamandframe.coreapi.modules.cart.model.CartLine;
import com.roamandframe.coreapi.modules.cart.repository.CartRepository;
import com.roamandframe.coreapi.modules.catalog.model.Product;
import com.roamandframe.coreapi.modules.catalog.model.WithStock;
import com.roamandframe.coreapi.modules.catalog.service.CatalogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CatalogService catalogService;

    public CartService(CartRepository cartRepository, CatalogService catalogService) {
        this.cartRepository = cartRepository;
        this.catalogService = catalogService;
    }

    @Transactional(readOnly = true)
    public Cart getCart(UUID customerId) {
        return buildCart(customerId);
    }

    /** Sets the exact quantity of a SKU in the cart; 0 removes it. */
    @Transactional
    public Cart setItemQuantity(UUID customerId, String sku, int quantity) {
        if (quantity == 0) {
            cartRepository.deleteItem(customerId, sku);
        } else {
            WithStock<Product> product = catalogService.getProduct(sku);
            if (quantity > product.quantity()) {
                throw new InsufficientStockException(sku, quantity, product.quantity());
            }
            cartRepository.saveItem(customerId, sku, quantity);
        }
        return buildCart(customerId);
    }

    @Transactional
    public void clearCart(UUID customerId) {
        cartRepository.deleteAll(customerId);
    }

    private Cart buildCart(UUID customerId) {
        List<CartItem> items = cartRepository.findItems(customerId);
        Map<String, WithStock<Product>> products = items.isEmpty()
                ? Map.of()
                : catalogService.getProducts(items.stream().map(CartItem::sku).toList());

        List<CartLine> lines = items.stream().map(i -> toLine(i, products.get(i.sku()))).toList();
        BigDecimal total = lines.stream()
                .map(CartLine::lineTotal)
                .filter(t -> t != null)
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
        return new Cart(lines, total);
    }

    private CartLine toLine(CartItem item, WithStock<Product> product) {
        if (product == null) {
            return new CartLine(item.sku(), null, null, item.quantity(), null, 0);
        }
        BigDecimal unitPrice = product.item().price();
        return new CartLine(item.sku(), product.item().name(), unitPrice, item.quantity(),
                unitPrice.multiply(BigDecimal.valueOf(item.quantity())), product.quantity());
    }
}
