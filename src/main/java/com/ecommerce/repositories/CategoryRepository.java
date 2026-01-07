package com.ecommerce.repositories;

import com.ecommerce.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Category entity operations.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    /**
     * Find a category by name.
     */
    Optional<Category> findByName(String name);
    
    /**
     * Find all top-level categories (no parent).
     */
    List<Category> findByParentIsNull();
    
    /**
     * Find subcategories of a parent category.
     */
    List<Category> findByParentId(Long parentId);
}
