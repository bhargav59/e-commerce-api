package com.ecommerce.services;

import com.ecommerce.dto.*;
import com.ecommerce.entities.*;
import com.ecommerce.exceptions.BadRequestException;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.CartItemRepository;
import com.ecommerce.repositories.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for shopping cart operations.
 */
@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;
    
    /**
     * Get cart for user.
     */
    public CartDTO getCart(User user) {
        Cart cart = cartRepository.findByUserIdWithItems(user.getId())
                .orElseGet(() -> createCart(user));
        return toDTO(cart);
    }
    
    /**
     * Add item to cart.
     */
    @Transactional
    public CartDTO addToCart(User user, AddToCartRequest request) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));
        
        Product product = productService.getProductEntityById(request.getProductId());
        
        // Check stock
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }
        
        // Check if item already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);
        
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        
        cart = cartRepository.save(cart);
        return toDTO(cart);
    }
    
    /**
     * Update cart item quantity.
     */
    @Transactional
    public CartDTO updateCartItem(User user, Long itemId, int quantity) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));
        
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));
        
        // Verify item belongs to user's cart
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Item does not belong to your cart");
        }
        
        if (quantity <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            if (item.getProduct().getStockQuantity() < quantity) {
                throw new BadRequestException("Insufficient stock");
            }
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
        
        return toDTO(cart);
    }
    
    /**
     * Remove item from cart.
     */
    @Transactional
    public CartDTO removeFromCart(User user, Long itemId) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));
        
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "id", itemId));
        
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new BadRequestException("Item does not belong to your cart");
        }
        
        cart.getItems().remove(item);
        cartItemRepository.delete(item);
        
        return toDTO(cart);
    }
    
    /**
     * Clear cart.
     */
    @Transactional
    public void clearCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));
        
        cartItemRepository.deleteByCartId(cart.getId());
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    /**
     * Get cart entity for user.
     */
    public Cart getCartEntity(User user) {
        return cartRepository.findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "userId", user.getId()));
    }
    
    /**
     * Create a new cart for user.
     */
    private Cart createCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .build();
        return cartRepository.save(cart);
    }
    
    /**
     * Convert Cart entity to DTO.
     */
    private CartDTO toDTO(Cart cart) {
        List<CartItemDTO> items = cart.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        
        BigDecimal total = items.stream()
                .map(CartItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalItems = items.stream()
                .mapToInt(CartItemDTO::getQuantity)
                .sum();
        
        return CartDTO.builder()
                .id(cart.getId())
                .items(items)
                .totalAmount(total)
                .totalItems(totalItems)
                .build();
    }
    
    private CartItemDTO toItemDTO(CartItem item) {
        Product product = item.getProduct();
        return CartItemDTO.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .productPrice(product.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
}
