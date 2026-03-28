package com.ecommerce.backend.service;

import com.ecommerce.backend.models.Cart;
import com.ecommerce.backend.models.CartItem;
import com.ecommerce.backend.models.Product;
import com.ecommerce.backend.models.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        product = new Product();
        product.setId(1L);
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStockQuantity(10);
    }

    @Test
    void testAddOrUpdateCartItem_NewItem() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.addOrUpdateCartItem("testuser", 1L, 2);

        assertNotNull(updatedCart);
        assertEquals(1, updatedCart.getItems().size());
        assertEquals(2, updatedCart.getItems().get(0).getQuantity());
        verify(cartRepository).save(any(Cart.class));
    }
}
