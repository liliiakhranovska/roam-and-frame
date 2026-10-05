package com.roamandframe.coreapi.security;

import com.roamandframe.coreapi.customer.model.Customer;

public interface TokenIssuer {
    String issueToken(Customer customer);
}
