package com.roamandframe.coreapi.modules.cart.repository;

import com.roamandframe.coreapi.modules.cart.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class CartRepositoryImpl implements CartRepository {

    private final CartItemJpaRepository jpaRepository;

    CartRepositoryImpl(CartItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<CartItem> findItems(UUID customerId) {
        return jpaRepository.findByCustomerIdOrderByCreatedAt(customerId).stream()
                .map(this::toItem)
                .toList();
    }

    @Override
    public void saveItem(UUID customerId, String sku, int quantity) {
        jpaRepository.findByCustomerIdAndSku(customerId, sku).ifPresentOrElse(
                entity -> entity.setQuantity(quantity),
                () -> jpaRepository.save(new CartItemJpaEntity(customerId, sku, quantity)));
    }

    @Override
    public void deleteItem(UUID customerId, String sku) {
        jpaRepository.deleteByCustomerIdAndSku(customerId, sku);
    }

    @Override
    public void deleteAll(UUID customerId) {
        jpaRepository.deleteByCustomerId(customerId);
    }

    private CartItem toItem(CartItemJpaEntity e) {
        return new CartItem(e.getSku(), e.getQuantity());
    }
}
