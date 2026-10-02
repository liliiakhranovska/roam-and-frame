package com.roamandframe.coreapi.api.security;

import com.roamandframe.coreapi.customer.model.Customer;

public interface TokenIssuer {
    String issueToken(Customer customer);
}
