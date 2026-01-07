package com.ecommerce.controllers;

import com.ecommerce.dto.CreateOrderRequest;
import com.ecommerce.dto.OrderDTO;
import com.ecommerce.entities.User;
import com.ecommerce.services.OrderService;
import com.ecommerce.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for order endpoints.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    
    /**
     * Get current user's orders.
     * GET /api/orders
     */
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders(Authentication authentication) {
        User user = getCurrentUser(authentication);
        List<OrderDTO> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * Get order by ID.
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(
            Authentication authentication,
            @PathVariable Long id) {
        User user = getCurrentUser(authentication);
        OrderDTO order = orderService.getOrderById(user, id);
        return ResponseEntity.ok(order);
    }
    
    /**
     * Create order from cart.
     * POST /api/orders
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            Authentication authentication,
            @Valid @RequestBody CreateOrderRequest request) {
        User user = getCurrentUser(authentication);
        OrderDTO order = orderService.createOrder(user, request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }
}
