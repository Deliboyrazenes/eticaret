package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.CategoryDTO;
import com.deliboyraz.eticaret.entity.Category;
import com.deliboyraz.eticaret.mapper.CategoryMapper;
import com.deliboyraz.eticaret.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
                .map(category -> new CategoryDTO(
                        category.getId(),
                        category.getName(),
                        null  // Sonsuz döngüyü engellemek için products null olarak set edildi
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.findCategoryById(id);
        // Tek bir kategori için ürünleri de getirebiliriz
        CategoryDTO categoryDTO = CategoryMapper.entityToDto(category);
        return ResponseEntity.ok(categoryDTO);
    }
}