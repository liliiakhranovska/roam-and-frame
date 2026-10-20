package com.roamandframe.coreapi.modules.customer.service;

import com.roamandframe.coreapi.modules.customer.model.CustomerAccount;
import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {
    CustomerProfile getProfile(UUID customerId);

    CustomerProfile updateProfile(UUID customerId, UpdateProfileCommand command);

    Optional<CustomerAccount> findAccountByEmail(String email);

    Map<UUID, String> findEmails(Collection<UUID> customerIds);
}
