package com.roamandframe.coreapi.customer.service;

import com.roamandframe.coreapi.customer.model.Customer;
import com.roamandframe.coreapi.customer.exception.InvalidCredentialsException;
import com.roamandframe.coreapi.customer.repository.CustomerCredentials;
import com.roamandframe.coreapi.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerAuthenticationService {

    private final CustomerRepository customerRepository;
    private final PasswordHasher passwordHasher;

    public CustomerAuthenticationService(CustomerRepository customerRepository,
                                         PasswordHasher passwordHasher) {
        this.customerRepository = customerRepository;
        this.passwordHasher = passwordHasher;
    }

    public Customer authenticate(String email, String rawPassword) {
        CustomerCredentials customerCredentials = customerRepository.findCredentialsByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(rawPassword, customerCredentials.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return customerCredentials.customer();
    }
}
