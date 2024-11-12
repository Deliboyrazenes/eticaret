package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.ReviewDTO;
import com.deliboyraz.eticaret.entity.Review;
import com.deliboyraz.eticaret.mapper.user.CustomerMapper;

public class ReviewMapper {
    public static ReviewDTO entityToDto(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewDTO(
                review.getId(),
                review.getDescription(),
                review.getRating(),
                ProductMapper.entityToDto(review.getProduct()),
                CustomerMapper.entityToDto(review.getCustomer())
        );
    }

    public static Review dtoToEntity(ReviewDTO reviewDTO) {
        if (reviewDTO == null) {
            return null;
        }
        Review review = new Review();
        review.setId(reviewDTO.id());
        review.setDescription(reviewDTO.description());
        review.setRating(reviewDTO.rating());
        review.setProduct(ProductMapper.dtoToEntity(reviewDTO.product()));
        review.setCustomer(CustomerMapper.dtoToEntity(reviewDTO.customer()));
        return review;
}
}
