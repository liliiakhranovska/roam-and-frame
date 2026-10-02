package com.roamandframe.coreapi.customer.repository;

import com.roamandframe.coreapi.customer.model.Customer;

public record CustomerCredentials (
    Customer customer,
    String passwordHash
) {

}
