package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.modules.cart.service.CartService;
import com.roamandframe.coreapi.modules.catalog.service.CatalogService;
import com.roamandframe.coreapi.modules.checkout.service.CheckoutService;
import com.roamandframe.coreapi.modules.customer.model.UpdateProfileCommand;
import com.roamandframe.coreapi.modules.customer.service.CustomerService;
import com.roamandframe.coreapi.modules.order.service.OrderService;
import com.roamandframe.coreapi.security.AuthenticationService;
import com.roamandframe.coreapi.security.CurrentCustomerId;
import com.roamandframe.coreapi.web.dto.AuthenticationRequest;
import com.roamandframe.coreapi.web.dto.AuthenticationResponse;
import com.roamandframe.coreapi.web.dto.CartResponse;
import com.roamandframe.coreapi.web.dto.OrderResponse;
import com.roamandframe.coreapi.web.dto.ProductResponse;
import com.roamandframe.coreapi.web.dto.ProductSummaryResponse;
import com.roamandframe.coreapi.web.dto.SetCartItemQuantityRequest;
import com.roamandframe.coreapi.web.dto.ProfileResponse;
import com.roamandframe.coreapi.web.dto.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.roamandframe.coreapi.modules.order.model.OrderDetails;
import com.roamandframe.coreapi.modules.order.model.OrderFilter;
import com.roamandframe.coreapi.modules.order.model.OrderStatus;
import com.roamandframe.coreapi.web.dto.AdminOrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    private final AuthenticationService authenticationService;
    private final CustomerService customerService;
    private final CatalogService catalogService;
    private final CartService cartService;
    private final CheckoutService checkoutService;
    private final OrderService orderService;

    public ApiController(AuthenticationService authenticationService,
                         CustomerService customerService,
                         CatalogService catalogService,
                         CartService cartService,
                         CheckoutService checkoutService,
                         OrderService orderService) {
        this.authenticationService = authenticationService;
        this.customerService = customerService;
        this.catalogService = catalogService;
        this.cartService = cartService;
        this.checkoutService = checkoutService;
        this.orderService = orderService;
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

    @Tag(name = "Cart")
    @GetMapping("/cart")
    public CartResponse getCart(@CurrentCustomerId UUID customerId) {
        return CartResponse.from(cartService.getCart(customerId));
    }

    @Tag(name = "Cart")
    @DeleteMapping("/cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@CurrentCustomerId UUID customerId) {
        cartService.clearCart(customerId);
    }

    @Tag(name = "Cart")
    @PutMapping("/cart/items/{sku}")
    public CartResponse setCartItemQuantity(@CurrentCustomerId UUID customerId,
                                            @PathVariable String sku,
                                            @Valid @RequestBody SetCartItemQuantityRequest request) {
        return CartResponse.from(cartService.setItemQuantity(customerId, sku, request.quantity()));
    }

    @Tag(name = "Checkout")
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(@CurrentCustomerId UUID customerId) {
        return OrderResponse.from(checkoutService.checkout(customerId));
    }

    @Tag(name = "Order")
    @GetMapping("/orders")
    public List<OrderResponse> getOrders(@CurrentCustomerId UUID customerId) {
        return orderService.getOrders(customerId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Tag(name = "Admin")
    @Operation(summary = "Search orders of all customers",
            description = "Newest first, at most 500 results. All parameters are optional and combined with AND. "
                    + "In-flight orders have status PLACED, past orders have status SHIPPED. "
                    + "from/to are dates (UTC) of the order creation, both inclusive. Requires the ADMIN role.")
    @GetMapping("/admin/orders")
    public List<AdminOrderResponse> searchOrders(
            @RequestParam(required = false) UUID customerId,
            @Parameter(description = "Repeat to match several statuses, e.g. status=PLACED&status=SHIPPED")
            @RequestParam(required = false) Set<OrderStatus> status,
            @Parameter(description = "First day, inclusive (yyyy-MM-dd, UTC)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Last day, inclusive (yyyy-MM-dd, UTC)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'to' must not be before 'from'");
        }
        Instant createdFrom = from == null ? null : from.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant createdBefore = to == null ? null : to.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        return toAdminResponses(orderService.searchOrders(new OrderFilter(customerId, status, createdFrom, createdBefore)));
    }

    @Tag(name = "Admin")
    @Operation(summary = "Mark an order as shipped",
            description = "Sets the order and its shipment to SHIPPED. Idempotent: an already shipped order "
                    + "is returned unchanged. Requires the ADMIN role.")
    @PostMapping("/admin/orders/{orderId}/ship")
    public AdminOrderResponse shipOrder(@PathVariable UUID orderId) {
        return toAdminResponses(List.of(orderService.markShipped(orderId))).getFirst();
    }

    private List<AdminOrderResponse> toAdminResponses(List<OrderDetails> orders) {
        Map<UUID, String> emails = customerService.findEmails(
                orders.stream().map(d -> d.order().customerId()).distinct().toList());
        return orders.stream()
                .map(d -> AdminOrderResponse.from(d, emails.get(d.order().customerId())))
                .toList();
    }
}
