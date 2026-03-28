package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.CartDTO;
import com.ecommerce.backend.dto.CartItemRequest;
import com.ecommerce.backend.models.Cart;
import com.ecommerce.backend.service.CartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    private final ModelMapper modelMapper;
    
    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Cart cart = cartService.getCartForUser(auth.getName());
        return ResponseEntity.ok(modelMapper.map(cart, CartDTO.class));
    }
    
    @PostMapping("/items")
    public ResponseEntity<CartDTO> addToCart(@RequestBody CartItemRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Cart cart = cartService.addOrUpdateCartItem(auth.getName(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(modelMapper.map(cart, CartDTO.class));
    }
    
    @PutMapping("/items/{productId}")
    public ResponseEntity<CartDTO> updateCartItem(@PathVariable Long productId, @RequestBody CartItemRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // Ignoring request.productId from body, using path variable for updates
        Cart cart = cartService.addOrUpdateCartItem(auth.getName(), productId, request.getQuantity());
        return ResponseEntity.ok(modelMapper.map(cart, CartDTO.class));
    }
    
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartDTO> removeCartItem(@PathVariable Long productId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Cart cart = cartService.removeItemFromCart(auth.getName(), productId);
        return ResponseEntity.ok(modelMapper.map(cart, CartDTO.class));
    }
}
