package com.deliboyraz.eticaret.entity;

import com.deliboyraz.eticaret.entity.user.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Cart", schema= "public")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_total")
    private BigDecimal itemTotal;

    @Column(name = "grand_total")
    private BigDecimal grandTotal;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "cart")
    private List<Order> orders;



    @ManyToMany
    @JoinTable(name = "cart_product",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();

    // Helper methods
    public void addProduct(Product product) {
        products.add(product);
        calculateTotals();
    }

    public void removeProduct(Product product) {
        products.remove(product);
        calculateTotals();
    }

    private void calculateTotals() {
        this.itemTotal = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.grandTotal = this.itemTotal;
    }
}