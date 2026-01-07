package com.ecommerce.repositories;

import com.ecommerce.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Profile entity operations.
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    
    /**
     * Find a profile by user ID.
     */
    Optional<Profile> findByUserId(Long userId);
}
