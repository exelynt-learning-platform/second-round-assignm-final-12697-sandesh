package com.ecommerce.backend.service;

import com.ecommerce.backend.models.*;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(50.00));

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2); // Total 100.00

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.getItems().add(item);
    }

    @Test
    void testCreateOrderFromCart() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        Order savedOrderMock = new Order();
        savedOrderMock.setId(1L);
        savedOrderMock.setTotalAmount(BigDecimal.valueOf(100.00));
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrderMock);

        Order newOrder = orderService.createOrderFromCart("testuser", "123 Test St");

        assertNotNull(newOrder);
        assertEquals(0, cart.getItems().size()); // Cart should be cleared
        verify(orderRepository).save(any(Order.class));
        verify(cartRepository).save(any(Cart.class));
    }
}
