package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.Cart;
import com.deliboyraz.eticaret.entity.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByCustomer(Customer customer);
}
