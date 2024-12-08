package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Kategori ID'sine göre ürünleri bulma
    List<Product> findByCategoryId(Long id);

    // İsme göre case-insensitive arama
    List<Product> findByNameContainingIgnoreCase(String keyword);
}