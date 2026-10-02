package com.roamandframe.coreapi.customer.model;

import java.util.UUID;

public record Customer(
        UUID id,
        String email,
        String firstName,
        String lastName
) {
}
