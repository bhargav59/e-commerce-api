package com.ecommerce.repositories;

import com.ecommerce.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Address entity operations.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
    /**
     * Find all addresses for a user.
     */
    List<Address> findByUserId(Long userId);
    
    /**
     * Find the default address for a user.
     */
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
}
