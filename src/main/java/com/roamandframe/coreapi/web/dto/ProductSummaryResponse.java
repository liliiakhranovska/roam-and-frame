package com.roamandframe.coreapi.web.dto;

import com.roamandframe.coreapi.modules.catalog.model.ProductSummary;
import com.roamandframe.coreapi.modules.catalog.model.WithStock;

import java.math.BigDecimal;

public record ProductSummaryResponse(
        String sku, String name, String brand, BigDecimal price, String categoryCode, int quantity
) {
    public static ProductSummaryResponse from(WithStock<ProductSummary> s) {
        ProductSummary p = s.item();
        return new ProductSummaryResponse(p.sku(), p.name(), p.brand(), p.price(), p.categoryCode(),
                s.quantity());
    }
}
