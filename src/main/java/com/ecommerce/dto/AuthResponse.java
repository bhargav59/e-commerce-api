package com.ecommerce.dto;

import lombok.*;

/**
 * DTO for authentication response containing JWT token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private UserDTO user;
    
    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
    }
}
