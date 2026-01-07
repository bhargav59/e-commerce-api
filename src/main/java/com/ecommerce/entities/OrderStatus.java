package com.ecommerce.entities;

/**
 * Enum representing order status throughout its lifecycle.
 */
public enum OrderStatus {
    PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
