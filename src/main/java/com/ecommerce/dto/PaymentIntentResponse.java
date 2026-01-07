package com.ecommerce.dto;

import lombok.*;

/**
 * DTO for Stripe payment intent response.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIntentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private Long amount;
    private String currency;
}
