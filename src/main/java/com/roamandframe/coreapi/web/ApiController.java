package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.service.CustomerProfileService;
import com.roamandframe.coreapi.security.AuthenticationService;
import com.roamandframe.coreapi.security.CurrentCustomerId;
import com.roamandframe.coreapi.web.dto.AuthenticationRequest;
import com.roamandframe.coreapi.web.dto.AuthenticationResponse;
import com.roamandframe.coreapi.web.dto.ProfileResponse;
import com.roamandframe.coreapi.web.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final AuthenticationService authenticationService;
    private final CustomerProfileService customerProfileService;

    public ApiController(AuthenticationService authenticationService,
                         CustomerProfileService customerProfileService) {
        this.authenticationService = authenticationService;
        this.customerProfileService = customerProfileService;
    }

    @PostMapping("/auth/token")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        String token = authenticationService.authenticate(request.email(), request.password());
        return new AuthenticationResponse(token, "Bearer");
    }

    @GetMapping("/customers/me")
    public ProfileResponse getProfile(@CurrentCustomerId UUID customerId) {
        return ProfileResponse.from(customerProfileService.getProfile(customerId));
    }

    @PutMapping("/customers/me")
    public ProfileResponse updateProfile(@CurrentCustomerId UUID customerId,
                                         @Valid @RequestBody UpdateProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(
                request.firstName(), request.lastName(), request.phone(),
                request.addressLine1(), request.addressLine2(), request.city(),
                request.region(), request.postalCode(), request.countryCode());
        return ProfileResponse.from(customerProfileService.updateProfile(customerId, command));
    }
}
