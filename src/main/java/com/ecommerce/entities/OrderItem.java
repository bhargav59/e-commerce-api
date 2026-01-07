package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

/**
 * Entity representing an item within an order.
 * Captures the product price at the time of purchase.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_at_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtTime;

    /**
     * Calculates the subtotal for this order item.
     */
    public BigDecimal getSubtotal() {
        return priceAtTime.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Creates an OrderItem from a CartItem, capturing the current price.
     */
    public static OrderItem fromCartItem(CartItem cartItem) {
        return OrderItem.builder()
                .product(cartItem.getProduct())
                .quantity(cartItem.getQuantity())
                .priceAtTime(cartItem.getProduct().getPrice())
                .build();
    }
}
