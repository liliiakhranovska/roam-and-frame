package com.roamandframe.coreapi.modules.shipping.repository;

import com.roamandframe.coreapi.modules.shipping.model.Shipment;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShipmentRepository {
    Shipment create(UUID orderId, UUID customerId, String trackingNumber);
    List<Shipment> findByOrderIds(Collection<UUID> orderIds);
}
