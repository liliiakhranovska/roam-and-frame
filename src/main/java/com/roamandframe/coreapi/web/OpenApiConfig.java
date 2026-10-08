package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.security.CurrentCustomerId;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.TreeMap;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Roam & Frame Core API", version = "v1"),
        tags = {@Tag(name = "Auth"), @Tag(name = "Customer"), @Tag(name = "Catalog"), @Tag(name = "Cart"), @Tag(name = "Checkout"), @Tag(name = "Order")},
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
class OpenApiConfig {

    static {
        // the customer id comes from the JWT, it is not a request parameter
        SpringDocUtils.getConfig().addAnnotationsToIgnore(CurrentCustomerId.class);
    }

    // list paths alphabetically; methods within a path keep the default GET, PUT, POST, DELETE order
    @Bean
    OpenApiCustomizer sortPaths() {
        return openApi -> {
            Paths sorted = new Paths();
            new TreeMap<>(openApi.getPaths()).forEach(sorted::addPathItem);
            openApi.setPaths(sorted);
        };
    }
}
