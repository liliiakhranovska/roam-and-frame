package com.roamandframe.coreapi.modules.catalog.service;

import com.roamandframe.coreapi.modules.catalog.exception.ProductNotFoundException;
import com.roamandframe.coreapi.modules.catalog.model.Product;
import com.roamandframe.coreapi.modules.catalog.model.ProductSummary;
import com.roamandframe.coreapi.modules.catalog.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CatalogService {

    private final CatalogRepository catalogRepository;

    public CatalogService(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductSummary> searchProducts(String categoryCode, String brand) {
        return catalogRepository.search(categoryCode, brand);
    }

    @Transactional(readOnly = true)
    public Product getProduct(String sku) {
        return catalogRepository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
    }
}
