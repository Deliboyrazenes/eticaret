package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
