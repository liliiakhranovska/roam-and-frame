package com.roamandframe.coreapi.modules.inventory.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock_items", schema = "inventory")
class StockItemJpaEntity {

    @Id
    private String sku;

    @Column(nullable = false)
    private int quantity;

    protected StockItemJpaEntity() {
    }

    String getSku() { return sku; }
    int getQuantity() { return quantity; }
}
