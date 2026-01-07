package com.ecommerce.repositories;

import com.ecommerce.entities.Order;
import com.ecommerce.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Find all orders for a user.
     */
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);
    
    /**
     * Find orders by status.
     */
    List<Order> findByStatus(OrderStatus status);
    
    /**
     * Find order by Stripe payment ID.
     */
    Optional<Order> findByStripePaymentId(String stripePaymentId);
    
    /**
     * Find order with items.
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items oi LEFT JOIN FETCH oi.product WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") Long orderId);
}
