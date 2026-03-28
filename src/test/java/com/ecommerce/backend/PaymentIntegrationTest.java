package com.ecommerce.backend;

import com.ecommerce.backend.models.*;
import com.ecommerce.backend.repository.*;
import com.ecommerce.backend.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PaymentIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testStripePaymentIntentCreation() {
        // 1. Create a User for the test
        User user = new User();
        user.setUsername("stripe_tester_" + System.currentTimeMillis());
        user.setEmail("stripe" + System.currentTimeMillis() + "@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);

        // 2. Mock a Product and an Order
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(new BigDecimal("99.99"));
        product.setStockQuantity(10);
        product = productRepository.save(product);

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress("Stripe Test Street");
        order.setTotalAmount(product.getPrice());

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(1);
        item.setPrice(product.getPrice());

        order.setItems(List.of(item));
        order = orderRepository.save(order);

        // 3. Test Stripe Intent Creation
        String clientSecret = paymentService.createPaymentIntent(order.getId(), user.getUsername());

        System.out.println("==========================================");
        System.out.println("STRIPE INTENT CREATED SUCCESSFULLY!");
        System.out.println("CLIENT SECRET: " + clientSecret);
        System.out.println("==========================================");

        assertNotNull(clientSecret);
        assertTrue(clientSecret.startsWith("pi_"));
    }
}
