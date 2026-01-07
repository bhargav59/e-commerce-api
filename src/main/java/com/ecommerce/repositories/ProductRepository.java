package com.ecommerce.repositories;

import com.ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for Product entity operations.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * Find all active products.
     */
    List<Product> findByIsActiveTrue();
    
    /**
     * Find products by category.
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Find active products by category.
     */
    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);
    
    /**
     * Search products by name (case-insensitive).
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products within a price range.
     */
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.isActive = true")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Find products with low stock.
     */
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);
}
