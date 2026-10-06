package com.roamandframe.coreapi.modules.customer.service;

import com.roamandframe.coreapi.modules.customer.model.CustomerAccount;
import com.roamandframe.coreapi.modules.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomerAccountService {

    private final CustomerRepository customerRepository;

    public CustomerAccountService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Optional<CustomerAccount> findByEmail(String email) {
        return customerRepository.findAccountByEmail(email);
    }
}
