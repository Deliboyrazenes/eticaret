package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
