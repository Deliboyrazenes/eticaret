package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.OrderDTO;
import com.deliboyraz.eticaret.entity.Order;
import com.deliboyraz.eticaret.mapper.user.CustomerMapper;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO entityToDto(Order order) {
        if (order == null) {
            return null;
        }
        return new OrderDTO(
                order.getId(),
                order.getOrderDate(),
                order.getAmount(),
                order.getStatus(),
                order.getShippingDate(),
                CustomerMapper.entityToDto(order.getCustomer()),
                PaymentMapper.entityToDto(order.getPayment()),
                order.getOrderItems().stream()
                        .map(OrderItemMapper::entityToDto)
                        .collect(Collectors.toList())
        );
    }

    public static Order dtoToEntity(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDTO.id());
        order.setOrderDate(orderDTO.orderDate());
        order.setAmount(orderDTO.amount());
        order.setStatus(orderDTO.status());
        order.setShippingDate(orderDTO.shippingDate());
        order.setCustomer(CustomerMapper.dtoToEntity(orderDTO.customer()));
        order.setPayment(PaymentMapper.dtoToEntity(orderDTO.payment()));
        order.setOrderItems(orderDTO.orderItems().stream()
                .map(OrderItemMapper::dtoToEntity)
                .collect(Collectors.toList()));
        return order;
    }
}
