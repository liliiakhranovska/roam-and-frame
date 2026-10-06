package com.roamandframe.coreapi.modules.customer.repository;

import com.roamandframe.coreapi.modules.customer.model.Customer;

public record CustomerCredentials (
    Customer customer,
    String passwordHash
) {

}
