package com.roamandframe.coreapi.modules.order.service;

import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.order.model.OrderFilter;
import com.roamandframe.coreapi.modules.order.model.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order create(UUID orderId, UUID customerId, List<OrderItem> items);

    List<OrderDetails> getOrders(UUID customerId);

    OrderDetails getOrder(UUID customerId, UUID orderId);

    OrderDetails markShipped(UUID orderId);

    List<OrderDetails> searchOrders(OrderFilter filter);
}
