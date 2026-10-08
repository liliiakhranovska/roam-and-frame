package com.roamandframe.coreapi.modules.catalog.service;

import com.roamandframe.coreapi.modules.catalog.exception.ProductNotFoundException;
import com.roamandframe.coreapi.modules.catalog.model.Product;
import com.roamandframe.coreapi.modules.catalog.model.ProductSummary;
import com.roamandframe.coreapi.modules.catalog.model.WithStock;
import com.roamandframe.coreapi.modules.catalog.repository.CatalogRepository;
import com.roamandframe.coreapi.modules.inventory.service.InventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CatalogService {

    private final CatalogRepository catalogRepository;
    private final InventoryService inventoryService;

    public CatalogService(CatalogRepository catalogRepository, InventoryService inventoryService) {
        this.catalogRepository = catalogRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional(readOnly = true)
    public List<WithStock<ProductSummary>> searchProducts(String categoryCode, String brand, Boolean inStock) {
        List<ProductSummary> products = catalogRepository.search(categoryCode, brand);
        Map<String, Integer> quantities = inventoryService.getQuantities(
                products.stream().map(ProductSummary::sku).toList());
        return products.stream()
                .map(p -> new WithStock<>(p, quantities.get(p.sku())))
                .filter(p -> inStock == null || (p.quantity() > 0) == inStock)
                .toList();
    }

    /** Products by SKU with their stock; SKUs that do not exist are absent from the result. */
    @Transactional(readOnly = true)
    public Map<String, WithStock<Product>> getProducts(Collection<String> skus) {
        List<Product> products = catalogRepository.findBySkus(skus);
        Map<String, Integer> quantities = inventoryService.getQuantities(
                products.stream().map(Product::sku).toList());
        return products.stream().collect(Collectors.toMap(
                Product::sku, p -> new WithStock<>(p, quantities.get(p.sku()))));
    }

    @Transactional(readOnly = true)
    public WithStock<Product> getProduct(String sku) {
        Product product = catalogRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
        return new WithStock<>(product, inventoryService.getQuantities(List.of(sku)).get(sku));
    }
}
