package com.ecommerce.dto;

import com.ecommerce.entities.Role;
import lombok.*;

/**
 * DTO for User data - excludes sensitive information like passwords.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
