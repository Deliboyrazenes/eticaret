package com.deliboyraz.eticaret.entity;

import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.enums.Ratings;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Review", schema="public")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "rating")
    private Ratings rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
