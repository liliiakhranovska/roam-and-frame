package com.roamandframe.coreapi.modules.customer.service;

import com.roamandframe.coreapi.modules.customer.exception.CustomerNotFoundException;
import com.roamandframe.coreapi.modules.customer.model.CustomerAccount;
import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public CustomerProfile getProfile(UUID customerId) {
        return customerRepository.findProfileById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional
    public CustomerProfile updateProfile(UUID customerId, UpdateProfileCommand command) {
        return customerRepository.updateProfile(customerId, command)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional(readOnly = true)
    public Optional<CustomerAccount> findAccountByEmail(String email) {
        return customerRepository.findAccountByEmail(email);
    }
}
