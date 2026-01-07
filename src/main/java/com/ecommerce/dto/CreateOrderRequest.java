package com.ecommerce.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for creating an order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    
    @NotNull(message = "Shipping address ID is required")
    private Long shippingAddressId;
}
