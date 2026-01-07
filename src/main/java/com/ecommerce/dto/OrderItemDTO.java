package com.ecommerce.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * DTO for Order item data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal priceAtTime;
    private BigDecimal subtotal;
}
