package com.deliboyraz.eticaret.mapper.user;

import com.deliboyraz.eticaret.dto.user.SellerDTO;
import com.deliboyraz.eticaret.entity.user.Seller;

public class SellerMapper {
    public static SellerDTO entityToDto(Seller seller) {
        if (seller == null) {
            return null;
        }
        return new SellerDTO(
                seller.getId(),
                seller.getName(),
                seller.getPhone()

        );
    }

    public static Seller dtoToEntity(SellerDTO sellerDTO) {
        if (sellerDTO == null) {
            return null;
        }
        Seller seller = new Seller();
        seller.setId(sellerDTO.id());
        seller.setName(sellerDTO.name());
        seller.setPhone(sellerDTO.phone());

        return seller;
}
}
