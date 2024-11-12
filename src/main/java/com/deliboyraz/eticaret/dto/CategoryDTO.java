package com.deliboyraz.eticaret.dto;

import java.util.List;

public record CategoryDTO(Long id,
                          String name,
                          List<ProductDTO> products){
}
