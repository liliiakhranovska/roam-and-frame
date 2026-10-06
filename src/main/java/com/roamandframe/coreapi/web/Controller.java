package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.security.TokenIssuer;
import com.roamandframe.coreapi.modules.customer.model.Customer;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.service.CustomerAuthenticationService;
import com.roamandframe.coreapi.modules.customer.service.CustomerProfileService;
import com.roamandframe.coreapi.web.dto.AuthenticationRequest;
import com.roamandframe.coreapi.web.dto.AuthenticationResponse;
import com.roamandframe.coreapi.web.dto.ProfileResponse;
import com.roamandframe.coreapi.web.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@RestController
public class Controller {

    private final CustomerAuthenticationService customerAuthenticationService;
    private final TokenIssuer tokenIssuer;
    private final CustomerProfileService customerProfileService;

    public Controller(CustomerAuthenticationService customerAuthenticationService,
                      TokenIssuer tokenIssuer, CustomerProfileService customerProfileService) {
        this.customerAuthenticationService = customerAuthenticationService;
        this.tokenIssuer = tokenIssuer;
        this.customerProfileService = customerProfileService;
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        Customer customer = customerAuthenticationService.authenticate(request.email(), request.password());
        return new AuthenticationResponse(tokenIssuer.issueToken(customer), "Bearer");
    }

    @GetMapping("/getProfileDetails")
    public ProfileResponse getProfileDetails(@AuthenticationPrincipal Jwt jwt) {
        UUID customerId = UUID.fromString(jwt.getSubject());
        return ProfileResponse.from(customerProfileService.getProfile(customerId));
    }

    @PutMapping("/updateProfileDetails")
    public ProfileResponse updateProfileDetails(@AuthenticationPrincipal Jwt jwt,
                                                @Valid @RequestBody UpdateProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(
                request.firstName(), request.lastName(), request.phone(),
                request.addressLine1(), request.addressLine2(), request.city(),
                request.region(), request.postalCode(), request.countryCode());
        return ProfileResponse.from(customerProfileService.updateProfile(customerId(jwt), command));
    }

    private static UUID customerId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}
