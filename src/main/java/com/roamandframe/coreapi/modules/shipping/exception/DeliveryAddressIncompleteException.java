package com.roamandframe.coreapi.modules.shipping.exception;

import java.util.List;

public class DeliveryAddressIncompleteException extends RuntimeException {
    public DeliveryAddressIncompleteException(List<String> missingFields) {
        super("Delivery address is incomplete, missing: " + String.join(", ", missingFields));
    }
}
