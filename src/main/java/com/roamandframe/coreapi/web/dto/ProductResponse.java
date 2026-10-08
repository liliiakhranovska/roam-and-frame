package com.roamandframe.coreapi.web.dto;

import com.roamandframe.coreapi.modules.catalog.model.Product;

import java.math.BigDecimal;
import java.util.Map;

public record ProductResponse(
        String sku, String name, String description, String brand, BigDecimal price,
        String categoryCode, String categoryName, Map<String, Object> attributes
) {
    public static ProductResponse from(Product p) {
        return new ProductResponse(p.sku(), p.name(), p.description(), p.brand(), p.price(),
                p.categoryCode(), p.categoryName(), p.attributes());
    }
}
