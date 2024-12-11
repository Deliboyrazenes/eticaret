package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.Order;
import com.deliboyraz.eticaret.entity.Payment;
import com.deliboyraz.eticaret.enums.PaymentMethods;
import com.deliboyraz.eticaret.repository.OrderRepository;
import com.deliboyraz.eticaret.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.EnumSet;

@Service
public class PaymentService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Payment processPayment(Order order, PaymentMethods paymentMethod) {
        if (paymentMethod == null || !EnumSet.allOf(PaymentMethods.class).contains(paymentMethod)) {
            throw new IllegalArgumentException("Geçersiz ödeme yöntemi: " + paymentMethod);
        }

        Payment payment = new Payment();
        payment.setMethod(paymentMethod);
        payment.setPaymentDate(LocalDate.now());
        payment.setOrder(order);
        payment.setCustomer(order.getCustomer());

        // Siparişi kaydet
        orderRepository.save(order);

        // Siparişle ödeme ilişkilendirilmeden önce kontrol
        order.setPayment(payment);

        // Ödemeyi kaydet
        return paymentRepository.save(payment);
    }
}
