package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.ProductDTO;
import com.deliboyraz.eticaret.entity.Category;
import com.deliboyraz.eticaret.entity.Product;
import com.deliboyraz.eticaret.mapper.user.SellerMapper;

public class ProductMapper {
    public static ProductDTO entityToDto(Product product) {
        if (product == null) {
            return null;
        }
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStock(),
                product.getBrand(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                SellerMapper.entityToDto(product.getSeller())
        );
    }

    public static Product dtoToEntity(ProductDTO productDTO) {
        if (productDTO == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDTO.id());
        product.setName(productDTO.name());
        product.setPrice(productDTO.price());
        product.setStock(productDTO.stock());
        product.setBrand(productDTO.brand());

        Category category = new Category();
        category.setId(productDTO.categoryId());
        category.setName(productDTO.categoryName());
        product.setCategory(category);

        product.setSeller(SellerMapper.dtoToEntity(productDTO.seller()));
        return product;
}
}
