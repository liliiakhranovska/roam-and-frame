package com.roamandframe.coreapi.modules.customer.service;

import com.roamandframe.coreapi.modules.customer.exception.CustomerNotFoundException;
import com.roamandframe.coreapi.modules.customer.model.CustomerAccount;
import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerProfile getProfile(UUID customerId) {
        return customerRepository.findProfileById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional
    @Override
    public CustomerProfile updateProfile(UUID customerId, UpdateProfileCommand command) {
        return customerRepository.updateProfile(customerId, command)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerAccount> findAccountByEmail(String email) {
        return customerRepository.findAccountByEmail(email);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<UUID, String> findEmails(Collection<UUID> customerIds) {
        return customerRepository.findEmailsByIds(customerIds);
    }
}
