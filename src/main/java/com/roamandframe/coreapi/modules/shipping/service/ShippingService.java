package com.roamandframe.coreapi.modules.shipping.service;

import com.roamandframe.coreapi.modules.shipping.model.Shipment;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface ShippingService {

    void checkDeliverable(UUID customerId);

    Shipment initiate(UUID orderId, UUID customerId);

    Map<UUID, Shipment> findByOrderIds(Collection<UUID> orderIds);
}
