package com.ecommerce.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * DTO for Cart item data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal productPrice;
    private Integer quantity;
    private BigDecimal subtotal;
}
