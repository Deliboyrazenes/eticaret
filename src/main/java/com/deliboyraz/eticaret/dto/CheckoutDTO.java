package com.deliboyraz.eticaret.dto;

public record CheckoutDTO(Long addressId,
                          String cardNumber,
                          String cardHolderName,
                          String expiryDate,
                          String cvv) {
}
