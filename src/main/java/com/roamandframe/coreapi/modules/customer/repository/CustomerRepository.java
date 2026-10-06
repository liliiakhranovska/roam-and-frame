package com.roamandframe.coreapi.modules.customer.repository;

import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<CustomerCredentials> findCredentialsByEmail(String email);
    Optional<CustomerProfile> findProfileById(UUID id);
    Optional<CustomerProfile> updateProfile(UUID id, UpdateProfileCommand command);
}

