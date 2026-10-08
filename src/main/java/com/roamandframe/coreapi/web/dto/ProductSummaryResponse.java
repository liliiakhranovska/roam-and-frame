package com.roamandframe.coreapi.web.dto;

import com.roamandframe.coreapi.modules.catalog.model.ProductSummary;

import java.math.BigDecimal;

public record ProductSummaryResponse(
        String sku, String name, String brand, BigDecimal price, String categoryCode
) {
    public static ProductSummaryResponse from(ProductSummary p) {
        return new ProductSummaryResponse(p.sku(), p.name(), p.brand(), p.price(), p.categoryCode());
    }
}
