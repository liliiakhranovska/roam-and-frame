package com.roamandframe.coreapi.modules.catalog.service;

import com.roamandframe.coreapi.modules.catalog.model.Product;
import com.roamandframe.coreapi.modules.catalog.model.ProductSummary;
import com.roamandframe.coreapi.modules.catalog.model.WithStock;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CatalogService {
    List<WithStock<ProductSummary>> searchProducts(String categoryCode, String brand, Boolean inStock);

    Map<String, WithStock<Product>> getProducts(Collection<String> skus);

    WithStock<Product> getProduct(String sku);
}
