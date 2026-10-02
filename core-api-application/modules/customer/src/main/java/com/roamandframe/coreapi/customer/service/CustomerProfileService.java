package com.roamandframe.coreapi.customer.service;

import com.roamandframe.coreapi.customer.exception.CustomerNotFoundException;
import com.roamandframe.coreapi.customer.model.CustomerProfile;
import com.roamandframe.coreapi.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerProfileService {

    private final CustomerRepository customerRepository;

    public CustomerProfileService(CustomerRepository customerRepository) {
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
}
