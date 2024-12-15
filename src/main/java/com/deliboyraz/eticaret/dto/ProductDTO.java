package com.deliboyraz.eticaret.dto;

import com.deliboyraz.eticaret.dto.user.SellerDTO;

import java.math.BigDecimal;

public record ProductDTO(Long id,
                         String name,
                         BigDecimal price,
                         Integer stock,
                         String brand,
                         Long categoryId,
                         String categoryName,
                         SellerDTO seller,
                         String imagePath) {
}
