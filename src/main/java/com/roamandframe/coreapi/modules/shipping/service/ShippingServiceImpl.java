package com.roamandframe.coreapi.modules.shipping.service;

import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.service.CustomerService;
import com.roamandframe.coreapi.modules.shipping.exception.DeliveryAddressIncompleteException;
import com.roamandframe.coreapi.modules.shipping.gateway.ShippingGateway;
import com.roamandframe.coreapi.modules.shipping.model.DeliveryAddress;
import com.roamandframe.coreapi.modules.shipping.model.Shipment;
import com.roamandframe.coreapi.modules.shipping.repository.ShipmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
class ShippingServiceImpl implements ShippingService {

    private final ShipmentRepository shipmentRepository;
    private final ShippingGateway shippingGateway;
    private final CustomerService customerService;

    public ShippingServiceImpl(ShipmentRepository shipmentRepository, ShippingGateway shippingGateway,
                               CustomerService customerService) {
        this.shipmentRepository = shipmentRepository;
        this.shippingGateway = shippingGateway;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    @Override
    public void checkDeliverable(UUID customerId) {
        deliveryAddressOf(customerId);
    }

    @Transactional
    @Override
    public Shipment initiate(UUID orderId, UUID customerId) {
        DeliveryAddress address = deliveryAddressOf(customerId);
        String trackingNumber = shippingGateway.createShipment(orderId, address);
        return shipmentRepository.create(orderId, customerId, trackingNumber);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<UUID, Shipment> findByOrderIds(Collection<UUID> orderIds) {
        return shipmentRepository.findByOrderIds(orderIds).stream()
                .collect(Collectors.toMap(Shipment::orderId, Function.identity()));
    }

    @Transactional
    @Override
    public void markShipped(UUID orderId) {
        shipmentRepository.markShipped(orderId);
    }

    private DeliveryAddress deliveryAddressOf(UUID customerId) {
        CustomerProfile p = customerService.getProfile(customerId);
        List<String> missing = new ArrayList<>();
        if (isBlank(p.addressLine1())) missing.add("addressLine1");
        if (isBlank(p.city())) missing.add("city");
        if (isBlank(p.postalCode())) missing.add("postalCode");
        if (isBlank(p.countryCode())) missing.add("countryCode");
        if (!missing.isEmpty()) {
            throw new DeliveryAddressIncompleteException(missing);
        }
        return new DeliveryAddress(p.addressLine1(), p.addressLine2(), p.city(),
                p.region(), p.postalCode(), p.countryCode());
    }

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
