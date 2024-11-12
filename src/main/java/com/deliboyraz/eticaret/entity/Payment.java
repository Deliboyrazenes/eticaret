package com.deliboyraz.eticaret.entity;

import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.enums.PaymentMethods;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Payment", schema="public")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "method")
    private PaymentMethods method;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
