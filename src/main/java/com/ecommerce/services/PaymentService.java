package com.ecommerce.services;

import com.ecommerce.dto.PaymentIntentResponse;
import com.ecommerce.entities.Order;
import com.ecommerce.entities.OrderStatus;
import com.ecommerce.exceptions.BadRequestException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for Stripe payment integration.
 */
@Service
@RequiredArgsConstructor
public class PaymentService {
    
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    
    private final OrderService orderService;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }
    
    /**
     * Create a payment intent for an order.
     */
    public PaymentIntentResponse createPaymentIntent(Long orderId) {
        Order order = orderService.getOrderEntityById(orderId);
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order is not in pending status");
        }
        
        // Convert amount to cents (Stripe uses smallest currency unit)
        long amountInCents = order.getTotalAmount().multiply(new java.math.BigDecimal("100")).longValue();
        
        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .putMetadata("orderId", orderId.toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();
            
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            
            return PaymentIntentResponse.builder()
                    .clientSecret(paymentIntent.getClientSecret())
                    .paymentIntentId(paymentIntent.getId())
                    .amount(amountInCents)
                    .currency("usd")
                    .build();
            
        } catch (StripeException e) {
            throw new BadRequestException("Payment creation failed: " + e.getMessage());
        }
    }
    
    /**
     * Handle successful payment (called from webhook or client confirmation).
     */
    public void handlePaymentSuccess(String paymentIntentId, Long orderId) {
        orderService.updateOrderPayment(orderId, paymentIntentId);
    }
    
    /**
     * Process webhook event (simplified - in production, verify signature).
     */
    public void processWebhookEvent(String eventType, String paymentIntentId, Long orderId) {
        switch (eventType) {
            case "payment_intent.succeeded":
                handlePaymentSuccess(paymentIntentId, orderId);
                break;
            case "payment_intent.payment_failed":
                // Handle failed payment - could notify user
                break;
            default:
                // Ignore other events
                break;
        }
    }
}
