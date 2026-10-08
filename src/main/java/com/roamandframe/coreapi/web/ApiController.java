package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.service.CustomerService;
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
    private final CustomerService customerService;

    public ApiController(AuthenticationService authenticationService,
                         CustomerService customerService) {
        this.authenticationService = authenticationService;
        this.customerService = customerService;
    }

    @PostMapping("/auth/token")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        String token = authenticationService.authenticate(request.email(), request.password());
        return new AuthenticationResponse(token, "Bearer");
    }

    @GetMapping("/customers/me")
    public ProfileResponse getProfile(@CurrentCustomerId UUID customerId) {
        return ProfileResponse.from(customerService.getProfile(customerId));
    }

    @PutMapping("/customers/me")
    public ProfileResponse updateProfile(@CurrentCustomerId UUID customerId,
                                         @Valid @RequestBody UpdateProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(
                request.firstName(), request.lastName(), request.phone(),
                request.addressLine1(), request.addressLine2(), request.city(),
                request.region(), request.postalCode(), request.countryCode());
        return ProfileResponse.from(customerService.updateProfile(customerId, command));
    }
}
