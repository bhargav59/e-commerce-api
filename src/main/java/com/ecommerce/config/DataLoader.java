package com.ecommerce.config;

import com.ecommerce.entities.*;
import com.ecommerce.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Configuration for loading sample data on startup.
 * Only runs in development mode.
 */
@Configuration
@RequiredArgsConstructor
public class DataLoader {
    
    private final PasswordEncoder passwordEncoder;
    
    @Bean
    @Profile("!test")
    public CommandLineRunner loadData(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            CartRepository cartRepository,
            AddressRepository addressRepository) {
        
        return args -> {
            // Only load if database is empty
            if (userRepository.count() > 0) {
                return;
            }
            
            System.out.println("Loading sample data...");
            
            // Create admin user
            User admin = User.builder()
                    .email("admin@ecommerce.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("User")
                    .role(Role.ADMIN)
                    .build();
            admin = userRepository.save(admin);
            
            // Create regular user
            User user = User.builder()
                    .email("user@ecommerce.com")
                    .password(passwordEncoder.encode("user123"))
                    .firstName("John")
                    .lastName("Doe")
                    .role(Role.USER)
                    .build();
            user = userRepository.save(user);
            
            // Create cart for user
            Cart cart = Cart.builder()
                    .user(user)
                    .build();
            cartRepository.save(cart);
            
            // Create address for user
            Address address = Address.builder()
                    .user(user)
                    .street("123 Main St")
                    .city("New York")
                    .state("NY")
                    .postalCode("10001")
                    .country("USA")
                    .isDefault(true)
                    .addressType(AddressType.SHIPPING)
                    .build();
            addressRepository.save(address);
            
            // Create categories
            Category electronics = Category.builder()
                    .name("Electronics")
                    .description("Electronic devices and accessories")
                    .build();
            electronics = categoryRepository.save(electronics);
            
            Category phones = Category.builder()
                    .name("Smartphones")
                    .description("Mobile phones and accessories")
                    .parent(electronics)
                    .build();
            phones = categoryRepository.save(phones);
            
            Category laptops = Category.builder()
                    .name("Laptops")
                    .description("Laptop computers")
                    .parent(electronics)
                    .build();
            laptops = categoryRepository.save(laptops);
            
            Category clothing = Category.builder()
                    .name("Clothing")
                    .description("Fashion and apparel")
                    .build();
            clothing = categoryRepository.save(clothing);
            
            // Create products
            Product iphone = Product.builder()
                    .name("iPhone 15 Pro")
                    .description("Latest Apple smartphone with A17 Pro chip")
                    .price(new BigDecimal("999.99"))
                    .stockQuantity(50)
                    .imageUrl("https://example.com/iphone15.jpg")
                    .category(phones)
                    .isActive(true)
                    .build();
            productRepository.save(iphone);
            
            Product samsung = Product.builder()
                    .name("Samsung Galaxy S24")
                    .description("Samsung flagship smartphone")
                    .price(new BigDecimal("899.99"))
                    .stockQuantity(40)
                    .imageUrl("https://example.com/galaxy24.jpg")
                    .category(phones)
                    .isActive(true)
                    .build();
            productRepository.save(samsung);
            
            Product macbook = Product.builder()
                    .name("MacBook Pro 16")
                    .description("Apple laptop with M3 chip")
                    .price(new BigDecimal("2499.99"))
                    .stockQuantity(25)
                    .imageUrl("https://example.com/macbook.jpg")
                    .category(laptops)
                    .isActive(true)
                    .build();
            productRepository.save(macbook);
            
            Product tshirt = Product.builder()
                    .name("Classic Cotton T-Shirt")
                    .description("Comfortable 100% cotton t-shirt")
                    .price(new BigDecimal("29.99"))
                    .stockQuantity(200)
                    .imageUrl("https://example.com/tshirt.jpg")
                    .category(clothing)
                    .isActive(true)
                    .build();
            productRepository.save(tshirt);
            
            System.out.println("Sample data loaded successfully!");
            System.out.println("Admin login: admin@ecommerce.com / admin123");
            System.out.println("User login: user@ecommerce.com / user123");
        };
    }
}
