package com.deliboyraz.eticaret.dto;

public record OrderItemDTO(Long id,
                           Integer quantity,
                           ProductDTO product)    {
}
