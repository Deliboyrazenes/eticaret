package com.deliboyraz.eticaret.dto;

import com.deliboyraz.eticaret.dto.user.CustomerDTO;

import java.math.BigDecimal;
import java.util.List;

public record CartDTO(Long id,
                      BigDecimal itemTotal,
                      BigDecimal grandTotal,
                      CustomerDTO customer,
                      List<ProductDTO> products){
        }
