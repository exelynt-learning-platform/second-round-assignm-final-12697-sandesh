package com.ecommerce.backend.service;

import com.ecommerce.backend.models.Cart;
import com.ecommerce.backend.models.CartItem;
import com.ecommerce.backend.models.Product;
import com.ecommerce.backend.models.User;
import com.ecommerce.backend.repository.CartRepository;
import com.ecommerce.backend.repository.ProductRepository;
import com.ecommerce.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Cart getCartForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    // public Cart addOrUpdateCartItem(String username, Long productId, Integer quantity) {
    //     Cart cart = getCartForUser(username);
        
    //     Optional<CartItem> existingItem = cart.getItems().stream()
    //             .filter(item -> item.getProduct().getId().equals(productId))
    //             .findFirst();

    //     if (existingItem.isPresent()) {
    //         CartItem item = existingItem.get();
    //         item.setQuantity(item.getQuantity() + quantity);

    //     } else {
    //         Product product = productRepository.findById(productId)
    //                 .orElseThrow(() -> new RuntimeException("Product not found"));
            
    //         CartItem newItem = new CartItem();
    //         newItem.setCart(cart);
    //         newItem.setProduct(product);
    //         newItem.setQuantity(quantity);
    //         cart.getItems().add(newItem);
    //     }

    //     return cartRepository.save(cart);
    // }

    public Cart addOrUpdateCartItem(String username, Long productId, Integer quantity) {
    Cart cart = getCartForUser(username);

    Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst();

    if (existingItem.isPresent()) {
        CartItem item = existingItem.get();

        int newQuantity = item.getQuantity() + quantity;

        //  Handle negative or zero quantity
        if (newQuantity <= 0) {
            cart.getItems().remove(item);
        } else {
            //  Stock validation
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock");
            }
            item.setQuantity(newQuantity);
        }

    } else {
        // Validate quantity
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        //  Stock validation
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);

        cart.getItems().add(newItem);
    }

    return cartRepository.save(cart);
}

    public Cart removeItemFromCart(String username, Long productId) {
        Cart cart = getCartForUser(username);
        
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        
        return cartRepository.save(cart);
    }
}
