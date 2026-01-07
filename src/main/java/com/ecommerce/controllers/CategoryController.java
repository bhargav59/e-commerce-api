package com.ecommerce.controllers;

import com.ecommerce.dto.CategoryDTO;
import com.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for category endpoints.
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    
    private final CategoryService categoryService;
    
    /**
     * Get all categories.
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Get top-level categories.
     * GET /api/categories/top
     */
    @GetMapping("/top")
    public ResponseEntity<List<CategoryDTO>> getTopLevelCategories() {
        List<CategoryDTO> categories = categoryService.getTopLevelCategories();
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Get category by ID.
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    
    /**
     * Get subcategories.
     * GET /api/categories/{id}/subcategories
     */
    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryDTO>> getSubcategories(@PathVariable Long id) {
        List<CategoryDTO> categories = categoryService.getSubcategories(id);
        return ResponseEntity.ok(categories);
    }
    
    /**
     * Create category (admin only).
     * POST /api/categories
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Map<String, Object> request) {
        String name = (String) request.get("name");
        String description = (String) request.get("description");
        String imageUrl = (String) request.get("imageUrl");
        Long parentId = request.get("parentId") != null ? 
                Long.valueOf(request.get("parentId").toString()) : null;
        
        CategoryDTO category = categoryService.createCategory(name, description, imageUrl, parentId);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    
    /**
     * Update category (admin only).
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        String imageUrl = request.get("imageUrl");
        
        CategoryDTO category = categoryService.updateCategory(id, name, description, imageUrl);
        return ResponseEntity.ok(category);
    }
    
    /**
     * Delete category (admin only).
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
