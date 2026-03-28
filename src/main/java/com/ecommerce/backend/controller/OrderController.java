package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.OrderDTO;
import com.ecommerce.backend.dto.OrderRequest;
import com.ecommerce.backend.models.Order;
import com.ecommerce.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Order order = orderService.createOrderFromCart(auth.getName(), request.getShippingAddress());
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getUserOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Order> orders = orderService.getUserOrders(auth.getName());
        List<OrderDTO> orderDTOs = orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderDetails(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Order order = orderService.getOrderById(id, auth.getName());
        return ResponseEntity.ok(modelMapper.map(order, OrderDTO.class));
    }
}
