package com.roamandframe.coreapi.modules.order.model;

import com.roamandframe.coreapi.modules.shipping.model.Shipment;

/** An order together with its shipment, which is null if none exists. */
public record OrderDetails(Order order, Shipment shipment) {
}
