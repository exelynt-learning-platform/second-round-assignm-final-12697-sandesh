package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.OrderDTO;
import com.ecommerce.backend.models.Order;
import com.ecommerce.backend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ModelMapper modelMapper;

    @PostMapping("/create-intent/{orderId}")
    public ResponseEntity<?> createIntent(@PathVariable Long orderId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String clientSecret = paymentService.createPaymentIntent(orderId, auth.getName());
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }

    // This would typically be a webhook, but we mock it as a post endpoint for testing
    @PostMapping("/success/{orderId}")
    public ResponseEntity<OrderDTO> paymentSuccess(@PathVariable Long orderId) {
        Order order = paymentService.handlePaymentSuccess(orderId);
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }
}
