package com.roamandframe.coreapi.security;

public interface AuthenticationService {
    String authenticate(String email, String rawPassword);
}
