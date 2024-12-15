package com.deliboyraz.eticaret.dto;

import com.deliboyraz.eticaret.dto.user.CustomerDTO;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderDTO(Long id,
                       LocalDate orderDate,
                       BigDecimal amount,
                       Status status,
                       LocalDate shippingDate,
                       CustomerDTO customer,
                       PaymentDTO payment,
                       List<OrderItemDTO>orderItems){
}

