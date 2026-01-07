package com.ecommerce.controllers;

import com.ecommerce.dto.AddToCartRequest;
import com.ecommerce.dto.CartDTO;
import com.ecommerce.entities.User;
import com.ecommerce.services.CartService;
import com.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for shopping cart endpoints.
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    private final UserService userService;
    
    /**
     * Get current user's cart.
     * GET /api/cart
     */
    @GetMapping
    public ResponseEntity<CartDTO> getCart(Authentication authentication) {
        User user = getCurrentUser(authentication);
        CartDTO cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Add item to cart.
     * POST /api/cart/items
     */
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addToCart(
            Authentication authentication,
            @Valid @RequestBody AddToCartRequest request) {
        User user = getCurrentUser(authentication);
        CartDTO cart = cartService.addToCart(user, request);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Update cart item quantity.
     * PUT /api/cart/items/{itemId}
     */
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDTO> updateCartItem(
            Authentication authentication,
            @PathVariable Long itemId,
            @RequestBody Map<String, Integer> request) {
        User user = getCurrentUser(authentication);
        int quantity = request.getOrDefault("quantity", 1);
        CartDTO cart = cartService.updateCartItem(user, itemId, quantity);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Remove item from cart.
     * DELETE /api/cart/items/{itemId}
     */
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDTO> removeFromCart(
            Authentication authentication,
            @PathVariable Long itemId) {
        User user = getCurrentUser(authentication);
        CartDTO cart = cartService.removeFromCart(user, itemId);
        return ResponseEntity.ok(cart);
    }
    
    /**
     * Clear cart.
     * DELETE /api/cart
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        User user = getCurrentUser(authentication);
        cartService.clearCart(user);
        return ResponseEntity.noContent().build();
    }
    
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }
}
