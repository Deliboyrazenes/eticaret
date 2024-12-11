package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.OrderItem;
import com.deliboyraz.eticaret.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public OrderItem save(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
}
}
