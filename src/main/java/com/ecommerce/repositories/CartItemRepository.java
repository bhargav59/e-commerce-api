package com.ecommerce.repositories;

import com.ecommerce.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for CartItem entity operations.
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    /**
     * Find a cart item by cart ID and product ID.
     */
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
    
    /**
     * Delete all items from a cart.
     */
    void deleteByCartId(Long cartId);
}
