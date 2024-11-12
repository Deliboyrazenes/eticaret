package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.CategoryDTO;
import com.deliboyraz.eticaret.entity.Category;

import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDTO entityToDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getProducts().stream()
                        .map(ProductMapper::entityToDto)
                        .collect(Collectors.toList())
        );
    }

    public static Category dtoToEntity(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryDTO.id());
        category.setName(categoryDTO.name());
        category.setProducts(categoryDTO.products().stream()
                .map(ProductMapper::dtoToEntity)
                .collect(Collectors.toList()));
        return category;
}
}
