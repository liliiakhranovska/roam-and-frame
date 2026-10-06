package com.roamandframe.coreapi.modules.customer.service;

public interface PasswordHasher {
    boolean matches(String rawPassword, String hashedPassword);
}
