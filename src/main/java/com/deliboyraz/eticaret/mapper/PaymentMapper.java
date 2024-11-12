package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.PaymentDTO;
import com.deliboyraz.eticaret.entity.Payment;

public class PaymentMapper {
    public static PaymentDTO entityToDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        return new PaymentDTO(
                payment.getId(),
                payment.getMethod(),
                payment.getPaymentDate()
//                OrderMapper.entityToDto(payment.getOrder()),

        );
    }

    public static Payment dtoToEntity(PaymentDTO paymentDTO) {
        if (paymentDTO == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(paymentDTO.id());
        payment.setMethod(paymentDTO.method());
        payment.setPaymentDate(paymentDTO.paymentDate());

        return payment;
}
}
