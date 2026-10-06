package com.roamandframe.coreapi.security;

import com.roamandframe.coreapi.modules.customer.model.Customer;

public interface TokenIssuer {
    String issueToken(Customer customer);
}
