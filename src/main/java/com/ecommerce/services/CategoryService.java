package com.ecommerce.services;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.entities.Category;
import com.ecommerce.exceptions.BadRequestException;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for category operations.
 */
@Service
@RequiredArgsConstructor
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    /**
     * Get all categories.
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get top-level categories.
     */
    public List<CategoryDTO> getTopLevelCategories() {
        return categoryRepository.findByParentIsNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get category by ID.
     */
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return toDTO(category);
    }
    
    /**
     * Get subcategories of a parent.
     */
    public List<CategoryDTO> getSubcategories(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Create category (admin only).
     */
    @Transactional
    public CategoryDTO createCategory(String name, String description, String imageUrl, Long parentId) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new BadRequestException("Category with name already exists: " + name);
        }
        
        Category category = Category.builder()
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .build();
        
        if (parentId != null) {
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", parentId));
            category.setParent(parent);
        }
        
        category = categoryRepository.save(category);
        return toDTO(category);
    }
    
    /**
     * Update category (admin only).
     */
    @Transactional
    public CategoryDTO updateCategory(Long id, String name, String description, String imageUrl) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        
        category.setName(name);
        category.setDescription(description);
        category.setImageUrl(imageUrl);
        
        category = categoryRepository.save(category);
        return toDTO(category);
    }
    
    /**
     * Delete category (admin only).
     */
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
    }
    
    /**
     * Convert Category entity to DTO.
     */
    private CategoryDTO toDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .parentName(category.getParent() != null ? category.getParent().getName() : null)
                .build();
    }
}
