package com.ecommerce.backend.repository;

import com.ecommerce.backend.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
