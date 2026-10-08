package com.roamandframe.coreapi.modules.shipping.repository;

import com.roamandframe.coreapi.modules.shipping.model.Shipment;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
class ShipmentRepositoryImpl implements ShipmentRepository {

    private final ShipmentJpaRepository jpaRepository;

    ShipmentRepositoryImpl(ShipmentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Shipment create(UUID orderId, UUID customerId, String trackingNumber) {
        return toShipment(jpaRepository.saveAndFlush(new ShipmentJpaEntity(orderId, customerId, trackingNumber)));
    }

    @Override
    public List<Shipment> findByOrderIds(Collection<UUID> orderIds) {
        return jpaRepository.findByOrderIdIn(orderIds).stream().map(this::toShipment).toList();
    }

    private Shipment toShipment(ShipmentJpaEntity e) {
        return new Shipment(e.getId(), e.getOrderId(), e.getCustomerId(), e.getStatus(), e.getTrackingNumber());
    }
}
