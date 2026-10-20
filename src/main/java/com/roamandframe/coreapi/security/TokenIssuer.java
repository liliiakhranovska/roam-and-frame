package com.roamandframe.coreapi.security;

import com.roamandframe.coreapi.modules.customer.model.CustomerRole;

import java.util.UUID;

public interface TokenIssuer {
    String issueToken(UUID customerId, String email, CustomerRole role);
}
