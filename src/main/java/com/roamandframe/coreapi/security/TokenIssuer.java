package com.roamandframe.coreapi.security;

import java.util.UUID;

public interface TokenIssuer {
    String issueToken(UUID customerId, String email);
}
