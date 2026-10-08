package com.roamandframe.coreapi.web;

import com.roamandframe.coreapi.security.CurrentCustomerId;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Roam & Frame Core API", version = "v1"),
        tags = {@Tag(name = "Auth"), @Tag(name = "Customer"), @Tag(name = "Catalog")},
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
class OpenApiConfig {

    static {
        // the customer id comes from the JWT, it is not a request parameter
        SpringDocUtils.getConfig().addAnnotationsToIgnore(CurrentCustomerId.class);
    }
}
