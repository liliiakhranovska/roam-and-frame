package com.roamandframe.coreapi.modules.checkout.service;

import com.roamandframe.coreapi.modules.cart.model.Cart;
import com.roamandframe.coreapi.modules.cart.model.CartLine;
import com.roamandframe.coreapi.modules.cart.service.CartService;
import com.roamandframe.coreapi.modules.inventory.service.InventoryService;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.order.model.OrderItem;
import com.roamandframe.coreapi.modules.order.service.OrderService;
import com.roamandframe.coreapi.modules.payment.model.Payment;
import com.roamandframe.coreapi.modules.payment.service.PaymentService;
import com.roamandframe.coreapi.modules.shipping.service.ShippingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Orchestrates a checkout in one database transaction: any failure rolls back the stock reservation, the
 * order and the shipment record. Only the external payment authorization needs an explicit undo.
 */
@Service
public class CheckoutService {

    private final CartService cartService;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final ShippingService shippingService;

    public CheckoutService(CartService cartService, InventoryService inventoryService,
                           PaymentService paymentService, OrderService orderService,
                           ShippingService shippingService) {
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.orderService = orderService;
        this.shippingService = shippingService;
    }

    @Transactional
    public OrderDetails checkout(UUID customerId) {
        UUID orderId = UUID.randomUUID();

        Cart cart = cartService.validateCart(customerId);
        shippingService.checkDeliverable(customerId);
        inventoryService.reserve(cart.lines().stream()
                .collect(Collectors.toMap(CartLine::sku, CartLine::quantity)));

        Payment payment = paymentService.authorize(orderId, customerId, cart.total());
        try {
            orderService.create(orderId, customerId, cart.lines().stream().map(this::toOrderItem).toList());
            shippingService.initiate(orderId, customerId);
            cartService.clearCart(customerId);
            return orderService.getOrder(customerId, orderId);
        } catch (RuntimeException e) {
            paymentService.voidAuthorization(payment);
            throw e;
        }
    }

    private OrderItem toOrderItem(CartLine line) {
        return new OrderItem(line.sku(), line.name(), line.unitPrice(), line.quantity(), line.lineTotal());
    }
}
