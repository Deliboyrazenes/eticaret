package com.deliboyraz.eticaret.dto.user;

import com.deliboyraz.eticaret.enums.Genders;

public record CustomerDTO(Long id,
                          String firsName,
                          String lastName,
                          Genders genders,
                          Integer age) {
}
