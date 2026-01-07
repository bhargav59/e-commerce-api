package com.ecommerce.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO for Cart data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    private Long id;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;
    private Integer totalItems;
}
