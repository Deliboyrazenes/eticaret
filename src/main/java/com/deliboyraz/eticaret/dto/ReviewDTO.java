package com.deliboyraz.eticaret.dto;

import com.deliboyraz.eticaret.dto.user.CustomerDTO;
import com.deliboyraz.eticaret.enums.Ratings;

public record ReviewDTO(Long id,
                        String description,
                        Ratings rating,
                        ProductDTO product,
                        CustomerDTO customer){
}
