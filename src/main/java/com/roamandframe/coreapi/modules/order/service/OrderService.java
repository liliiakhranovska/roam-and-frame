package com.roamandframe.coreapi.modules.order.service;

import com.roamandframe.coreapi.modules.order.exception.OrderNotFoundException;
import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.order.model.OrderItem;
import com.roamandframe.coreapi.modules.order.repository.OrderRepository;
import com.roamandframe.coreapi.modules.shipping.model.Shipment;
import com.roamandframe.coreapi.modules.shipping.service.ShippingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ShippingService shippingService;

    public OrderService(OrderRepository orderRepository, ShippingService shippingService) {
        this.orderRepository = orderRepository;
        this.shippingService = shippingService;
    }

    @Transactional
    public Order create(UUID orderId, UUID customerId, List<OrderItem> items) {
        BigDecimal total = items.stream().map(OrderItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return orderRepository.create(orderId, customerId, items, total);
    }

    @Transactional(readOnly = true)
    public List<OrderDetails> getOrders(UUID customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        if (orders.isEmpty()) {
            return List.of();
        }
        Map<UUID, Shipment> shipments = shippingService.findByOrderIds(orders.stream().map(Order::id).toList());
        return orders.stream().map(o -> new OrderDetails(o, shipments.get(o.id()))).toList();
    }

    @Transactional(readOnly = true)
    public OrderDetails getOrder(UUID customerId, UUID orderId) {
        Order order = orderRepository.findByIdAndCustomerId(orderId, customerId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return new OrderDetails(order, shippingService.findByOrderIds(List.of(orderId)).get(orderId));
    }
}
