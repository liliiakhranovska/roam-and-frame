package com.roamandframe.coreapi.customer.service;

public interface PasswordHasher {
    boolean matches(String rawPassword, String hashedPassword);
}
