package com.roamandframe.coreapi.modules.cart.service;

import com.roamandframe.coreapi.modules.cart.exception.EmptyCartException;
import com.roamandframe.coreapi.modules.cart.exception.InsufficientStockException;
import com.roamandframe.coreapi.modules.cart.exception.InvalidCartException;
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
import java.util.Objects;
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

    /**
     * Locks the customer's cart rows (so a second checkout waits for the first) and checks that the cart
     * is not empty, every product still exists and enough stock is available. Returns the validated cart.
     */
    @Transactional
    public Cart validateCart(UUID customerId) {
        List<CartItem> items = cartRepository.findItemsForUpdate(customerId);
        if (items.isEmpty()) {
            throw new EmptyCartException();
        }
        Cart cart = toCart(items);
        List<String> problems = cart.lines().stream().map(this::problemOf).filter(Objects::nonNull).toList();
        if (!problems.isEmpty()) {
            throw new InvalidCartException(problems);
        }
        return cart;
    }

    @Transactional
    public void clearCart(UUID customerId) {
        cartRepository.deleteAll(customerId);
    }

    private Cart buildCart(UUID customerId) {
        return toCart(cartRepository.findItems(customerId));
    }

    private Cart toCart(List<CartItem> items) {
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

    private String problemOf(CartLine line) {
        if (line.name() == null) {
            return line.sku() + ": product is no longer available";
        }
        if (line.quantity() > line.availableQuantity()) {
            return line.sku() + ": requested " + line.quantity() + ", available " + line.availableQuantity();
        }
        return null;
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
