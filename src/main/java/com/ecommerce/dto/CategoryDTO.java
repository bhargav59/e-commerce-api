package com.ecommerce.dto;

import lombok.*;

/**
 * DTO for Category data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long parentId;
    private String parentName;
}
