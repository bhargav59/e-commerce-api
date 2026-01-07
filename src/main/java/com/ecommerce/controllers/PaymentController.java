package com.ecommerce.controllers;

import com.ecommerce.dto.PaymentIntentResponse;
import com.ecommerce.entities.User;
import com.ecommerce.services.PaymentService;
import com.ecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for payment endpoints.
 */
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    private final UserService userService;
    
    /**
     * Create a payment intent for an order.
     * POST /api/payments/create-intent
     */
    @PostMapping("/create-intent")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(
            Authentication authentication,
            @RequestBody Map<String, Long> request) {
        // Verify user is authenticated
        getCurrentUser(authentication);
        
        Long orderId = request.get("orderId");
        PaymentIntentResponse response = paymentService.createPaymentIntent(orderId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Handle Stripe webhook events.
     * POST /api/payments/webhook
     * 
     * Note: In production, you should verify the Stripe signature.
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        String eventType = (String) payload.get("type");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        @SuppressWarnings("unchecked")
        Map<String, Object> object = (Map<String, Object>) data.get("object");
        
        String paymentIntentId = (String) object.get("id");
        
        @SuppressWarnings("unchecked")
        Map<String, String> metadata = (Map<String, String>) object.get("metadata");
        Long orderId = metadata != null && metadata.get("orderId") != null ?
                Long.parseLong(metadata.get("orderId")) : null;
        
        if (orderId != null) {
            paymentService.processWebhookEvent(eventType, paymentIntentId, orderId);
        }
        
        return ResponseEntity.ok("Webhook received");
    }
    
    /**
     * Confirm payment (for client-side confirmation).
     * POST /api/payments/confirm
     */
    @PostMapping("/confirm")
    public ResponseEntity<String> confirmPayment(
            Authentication authentication,
            @RequestBody Map<String, Object> request) {
        getCurrentUser(authentication);
        
        String paymentIntentId = (String) request.get("paymentIntentId");
        Long orderId = Long.valueOf(request.get("orderId").toString());
        
        paymentService.handlePaymentSuccess(paymentIntentId, orderId);
        return ResponseEntity.ok("Payment confirmed");
    }
    
    private User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }
}
