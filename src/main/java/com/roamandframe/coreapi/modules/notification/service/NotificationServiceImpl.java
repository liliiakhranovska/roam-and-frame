package com.roamandframe.coreapi.modules.notification.service;

import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.service.CustomerService;
import com.roamandframe.coreapi.modules.notification.gateway.EmailSender;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.payment.model.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final EmailSender emailSender;
    private final CustomerService customerService;
    private final String currency;

    public NotificationServiceImpl(EmailSender emailSender, CustomerService customerService,
                                   @Value("${app.notification.currency}") String currency) {
        this.emailSender = emailSender;
        this.customerService = customerService;
        this.currency = currency;
    }

    @Async
    @Override
    public void sendOrderConfirmation(UUID customerId, OrderDetails details, Payment payment) {
        try {
            CustomerProfile customer = customerService.getProfile(customerId);
            emailSender.send(customer.email(),
                    NotificationMessages.orderConfirmationSubject(),
                    NotificationMessages.orderConfirmationBody(customer, details, payment, currency));
            log.info("Order confirmation sent: order={}, customer={}", details.order().id(), customerId);
        } catch (RuntimeException e) {
            log.error("Failed to send order confirmation: order={}, customer={}",
                    details.order().id(), customerId, e);
        }
    }
}
