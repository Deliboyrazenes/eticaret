package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findByCategoryId(Long id);
}
