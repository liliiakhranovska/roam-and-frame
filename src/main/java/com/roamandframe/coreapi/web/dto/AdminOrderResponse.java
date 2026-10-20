package com.roamandframe.coreapi.web.dto;

import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AdminOrderResponse(
        UUID id, UUID customerId, String customerEmail, String status, BigDecimal total, Instant createdAt,
        List<OrderItemResponse> items, ShipmentResponse shipment
) {
    public static AdminOrderResponse from(OrderDetails d, String customerEmail) {
        Order o = d.order();
        return new AdminOrderResponse(o.id(), o.customerId(), customerEmail, o.status().name(), o.total(),
                o.createdAt(), o.items().stream().map(OrderItemResponse::from).toList(),
                ShipmentResponse.from(d.shipment()));
    }
}
