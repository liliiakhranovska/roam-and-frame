package com.roamandframe.coreapi.modules.notification.service;

import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.payment.model.Payment;
import java.util.UUID;

public interface NotificationService {

    void sendOrderConfirmation(UUID customerId, OrderDetails details, Payment payment);
}
