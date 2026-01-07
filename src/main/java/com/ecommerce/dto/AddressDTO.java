package com.ecommerce.dto;

import lombok.*;

/**
 * DTO for Address data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Boolean isDefault;
    private String addressType;
}
