package com.deliboyraz.eticaret.dto;

import com.deliboyraz.eticaret.enums.PaymentMethods;

import java.time.LocalDate;

public record PaymentDTO(Long id,
                         PaymentMethods method,
                         LocalDate paymentDate){
}
