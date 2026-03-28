package com.ecommerce.backend.dto;

import com.ecommerce.backend.models.OrderStatus;
import com.ecommerce.backend.models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private String shippingAddress;
    private LocalDateTime createdAt;
}
