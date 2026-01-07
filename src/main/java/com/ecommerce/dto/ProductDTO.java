package com.ecommerce.dto;

import lombok.*;
import java.math.BigDecimal;

/**
 * DTO for Product data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
    private Boolean isActive;
}
