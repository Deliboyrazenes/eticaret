package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.OrderItemDTO;
import com.deliboyraz.eticaret.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemDTO entityToDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                ProductMapper.entityToDto(orderItem.getProduct())
        );
    }

    public static OrderItem dtoToEntity(OrderItemDTO orderItemDTO) {
        if (orderItemDTO == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemDTO.id());
        orderItem.setQuantity(orderItemDTO.quantity());
        orderItem.setProduct(ProductMapper.dtoToEntity(orderItemDTO.product()));
        return orderItem;
    }
}
