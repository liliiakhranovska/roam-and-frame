package com.roamandframe.coreapi.modules.shipping.gateway;

import com.roamandframe.coreapi.modules.shipping.model.DeliveryAddress;

import java.util.UUID;

/** Port to the external carrier. */
public interface ShippingGateway {

    /** Books the shipment and returns the carrier's tracking number. */
    String createShipment(UUID orderId, DeliveryAddress address);
}
