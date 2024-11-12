package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
