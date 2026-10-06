package com.roamandframe.coreapi.modules.customer.repository;

import com.roamandframe.coreapi.modules.customer.model.Customer;
import com.roamandframe.coreapi.modules.customer.model.CustomerProfile;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository jpaRepository;

    CustomerRepositoryImpl(CustomerJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<CustomerCredentials> findCredentialsByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(entity -> new CustomerCredentials(
                        toCustomer(entity),
                        entity.getPasswordHash()
                        )
                );
    }

    @Override
    public Optional<CustomerProfile> findProfileById(UUID id) {
        return jpaRepository.findById(id).map(this::toProfile);
    }

    @Override
    public Optional<CustomerProfile> updateProfile(UUID id, UpdateProfileCommand command) {
        return jpaRepository.findById(id).map(entity -> {
            entity.updateProfile(command);
            return toProfile(entity);
        });
    }

    private Customer toCustomer(CustomerJpaEntity entity) {
        return new Customer(
                        entity.getId(),
                        entity.getEmail(),
                        entity.getFirstName(),
                        entity.getLastName()
                );
    }

    private CustomerProfile toProfile(CustomerJpaEntity e) {
        return new CustomerProfile(
                e.getId(), e.getEmail(), e.getFirstName(), e.getLastName(),
                e.getPhone(), e.getAddressLine1(), e.getAddressLine2(),
                e.getCity(), e.getRegion(), e.getPostalCode(), e.getCountryCode());
    }
}
