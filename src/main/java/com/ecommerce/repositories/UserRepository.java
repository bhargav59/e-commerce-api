package com.ecommerce.repositories;

import com.ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by their email address.
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if a user exists with the given email.
     */
    boolean existsByEmail(String email);
}
