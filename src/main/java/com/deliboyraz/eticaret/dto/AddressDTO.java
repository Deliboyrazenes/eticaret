package com.deliboyraz.eticaret.dto;

public record AddressDTO(
        Long id,
        String city,
        String state,
        String description,
        String postalCode
//        boolean isDefault // Varsayılan adres olup olmadığını belirtmek için
) {}