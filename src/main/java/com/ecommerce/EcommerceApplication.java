package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the E-commerce REST API application.
 * 
 * This Spring Boot application provides a complete backend for an e-commerce platform,
 * including:
 * - User authentication with JWT
 * - Product and category management
 * - Shopping cart functionality
 * - Order processing
 * - Stripe payment integration
 */
@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }
}
