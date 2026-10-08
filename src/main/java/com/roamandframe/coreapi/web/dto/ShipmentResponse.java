package com.roamandframe.coreapi.web.dto;

import com.roamandframe.coreapi.modules.shipping.model.Shipment;

public record ShipmentResponse(String status, String trackingNumber) {
    public static ShipmentResponse from(Shipment s) {
        return s == null ? null : new ShipmentResponse(s.status().name(), s.trackingNumber());
    }
}
