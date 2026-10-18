package com.roamandframe.coreapi.modules.notification.service;

import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.order.model.Order;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.payment.model.Payment;
import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class NotificationMessages {

    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm 'UTC'", Locale.ENGLISH).withZone(ZoneOffset.UTC);

    private NotificationMessages() {
    }

    static String orderConfirmationSubject() {
        return "Your Roam & Frame order is confirmed";
    }

    static String orderConfirmationBody(CustomerProfile customer, OrderDetails details,
                                        Payment payment, String currency) {
        Order order = details.order();

        String items = order.items().stream()
                .map(i -> "  %d x %s - %s".formatted(i.quantity(), i.name(), money(i.lineTotal(), currency)))
                .collect(Collectors.joining("\n"));

        String address = Stream.of(customer.addressLine1(), customer.addressLine2(),
                        customer.postalCode() + " " + customer.city(), customer.countryCode())
                .filter(s -> s != null && !s.isBlank())
                .map(s -> "  " + s)
                .collect(Collectors.joining("\n"));

        String tracking = details.shipment() == null
                ? "Not available yet"
                : details.shipment().trackingNumber();

        return """
                Hi %s,

                Thank you for your order! Here is your confirmation.

                Order: %s
                Placed: %s

                Items:
                %s

                Total: %s

                Payment: %s (reference %s)
                Tracking number: %s

                Delivery address:
                %s

                Roam & Frame
                """.formatted(customer.firstName(), order.id(), DATE_TIME.format(order.createdAt()),
                items, money(order.total(), currency), payment.status(), payment.reference(),
                tracking, address);
    }

    private static String money(BigDecimal amount, String currency) {
        return amount.toPlainString() + " " + currency;
    }
}
