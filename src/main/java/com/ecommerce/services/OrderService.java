package com.ecommerce.services;

import com.ecommerce.dto.*;
import com.ecommerce.entities.*;
import com.ecommerce.exceptions.BadRequestException;
import com.ecommerce.exceptions.ResourceNotFoundException;
import com.ecommerce.repositories.AddressRepository;
import com.ecommerce.repositories.OrderRepository;
import com.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for order operations.
 */
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    
    /**
     * Create order from cart.
     */
    @Transactional
    public OrderDTO createOrder(User user, CreateOrderRequest request) {
        Cart cart = cartService.getCartEntity(user);
        
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }
        
        Address shippingAddress = addressRepository.findById(request.getShippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", request.getShippingAddressId()));
        
        // Verify address belongs to user
        if (!shippingAddress.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("Address does not belong to you");
        }
        
        // Create order
        Order order = Order.builder()
                .user(user)
                .shippingAddress(shippingAddress)
                .status(OrderStatus.PENDING)
                .build();
        
        // Add items and calculate total
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            
            // Validate stock
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new BadRequestException("Insufficient stock for: " + product.getName());
            }
            
            // Create order item
            OrderItem orderItem = OrderItem.fromCartItem(cartItem);
            order.addItem(orderItem);
            
            total = total.add(orderItem.getSubtotal());
            
            // Reduce stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }
        
        order.setTotalAmount(total);
        order = orderRepository.save(order);
        
        // Clear cart
        cartService.clearCart(user);
        
        return toDTO(order);
    }
    
    /**
     * Get user's orders.
     */
    public List<OrderDTO> getUserOrders(User user) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(user.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Get order by ID.
     */
    public OrderDTO getOrderById(User user, Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        // Verify order belongs to user (unless admin)
        if (!order.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new BadRequestException("Order does not belong to you");
        }
        
        return toDTO(order);
    }
    
    /**
     * Get order entity by ID.
     */
    public Order getOrderEntityById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    }
    
    /**
     * Update order status (admin or after payment).
     */
    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.setStatus(status);
        order = orderRepository.save(order);
        return toDTO(order);
    }
    
    /**
     * Update order with Stripe payment ID.
     */
    @Transactional
    public void updateOrderPayment(Long orderId, String stripePaymentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
        
        order.setStripePaymentId(stripePaymentId);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
    
    /**
     * Convert Order entity to DTO.
     */
    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> items = order.getItems().stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());
        
        AddressDTO addressDTO = null;
        if (order.getShippingAddress() != null) {
            Address addr = order.getShippingAddress();
            addressDTO = AddressDTO.builder()
                    .id(addr.getId())
                    .street(addr.getStreet())
                    .city(addr.getCity())
                    .state(addr.getState())
                    .postalCode(addr.getPostalCode())
                    .country(addr.getCountry())
                    .build();
        }
        
        return OrderDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .items(items)
                .shippingAddress(addressDTO)
                .build();
    }
    
    private OrderItemDTO toItemDTO(OrderItem item) {
        Product product = item.getProduct();
        return OrderItemDTO.builder()
                .id(item.getId())
                .productId(product.getId())
                .productName(product.getName())
                .productImageUrl(product.getImageUrl())
                .quantity(item.getQuantity())
                .priceAtTime(item.getPriceAtTime())
                .subtotal(item.getSubtotal())
                .build();
    }
}
