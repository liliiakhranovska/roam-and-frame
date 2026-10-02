package com.roamandframe.coreapi.customer.repository;

import com.roamandframe.coreapi.customer.model.UpdateProfileCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "customers", schema = "customer")
class CustomerJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CustomerJpaEntity() {
    }

    UUID getId() { return id; }
    String getEmail() { return email; }
    String getPasswordHash() { return passwordHash; }
    String getFirstName() { return firstName; }
    String getLastName() { return lastName; }
    String getPhone() {return phone; }
    String getAddressLine1() {return addressLine1;}
    String getAddressLine2() {return addressLine2;}
    String getCity() {return city;}
    String getRegion() {return region;}
    String getPostalCode() {return postalCode;}
    String getCountryCode() {return countryCode;}

    void updateProfile(UpdateProfileCommand cmd) {
        this.firstName = cmd.firstName();
        this.lastName = cmd.lastName();
        this.phone = cmd.phone();
        this.addressLine1 = cmd.addressLine1();
        this.addressLine2 = cmd.addressLine2();
        this.city = cmd.city();
        this.region = cmd.region();
        this.postalCode = cmd.postalCode();
        this.countryCode = cmd.countryCode();
        this.updatedAt = Instant.now();
    }
}



