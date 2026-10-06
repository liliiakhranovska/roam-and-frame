package com.roamandframe.coreapi.security;

public interface PasswordHasher {
    boolean matches(String rawPassword, String hashedPassword);
}
