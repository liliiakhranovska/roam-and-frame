package com.roamandframe.coreapi.modules.checkout.service;

import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import java.util.UUID;

public interface CheckoutService {
    OrderDetails checkout(UUID customerId);
}
