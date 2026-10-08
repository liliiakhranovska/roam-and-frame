package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.modules.catalog.service.CatalogService;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.service.CustomerService;
import com.roamandframe.coreapi.security.AuthenticationService;
import com.roamandframe.coreapi.security.CurrentCustomerId;
import com.roamandframe.coreapi.web.dto.AuthenticationRequest;
import com.roamandframe.coreapi.web.dto.AuthenticationResponse;
import com.roamandframe.coreapi.web.dto.ProductResponse;
import com.roamandframe.coreapi.web.dto.ProductSummaryResponse;
import com.roamandframe.coreapi.web.dto.ProfileResponse;
import com.roamandframe.coreapi.web.dto.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final CatalogService catalogService;

    public ApiController(AuthenticationService authenticationService,
                         CustomerService customerService,
                         CatalogService catalogService) {
        this.authenticationService = authenticationService;
        this.customerService = customerService;
        this.catalogService = catalogService;
    }

    @Tag(name = "Auth")
    @SecurityRequirements
    @PostMapping("/auth/token")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest request) {
        String token = authenticationService.authenticate(request.email(), request.password());
        return new AuthenticationResponse(token, "Bearer");
    }

    @Tag(name = "Customer")
    @GetMapping("/customer/profile")
    public ProfileResponse getProfile(@CurrentCustomerId UUID customerId) {
        return ProfileResponse.from(customerService.getProfile(customerId));
    }

    @Tag(name = "Customer")
    @PutMapping("/customer/profile")
    public ProfileResponse updateProfile(@CurrentCustomerId UUID customerId,
                                         @Valid @RequestBody UpdateProfileRequest request) {
        UpdateProfileCommand command = new UpdateProfileCommand(
                request.firstName(), request.lastName(), request.phone(),
                request.addressLine1(), request.addressLine2(), request.city(),
                request.region(), request.postalCode(), request.countryCode());
        return ProfileResponse.from(customerService.updateProfile(customerId, command));
    }

    @Tag(name = "Catalog")
    @GetMapping("/products")
    public List<ProductSummaryResponse> searchProducts(@RequestParam(required = false) String category,
                                                       @RequestParam(required = false) String brand,
                                                       @RequestParam(required = false) Boolean inStock) {
        return catalogService.searchProducts(category, brand, inStock).stream()
                .map(ProductSummaryResponse::from)
                .toList();
    }

    @Tag(name = "Catalog")
    @GetMapping("/products/{sku}")
    public ProductResponse getProduct(@PathVariable String sku) {
        return ProductResponse.from(catalogService.getProduct(sku));
    }
}
