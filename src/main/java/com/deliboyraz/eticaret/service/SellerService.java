package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.user.Seller;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.repository.user.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerService {
    private SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;

    }

    public Seller findById(Long id) throws NotFoundException{
        Optional<Seller> seller = sellerRepository.findById(id);
        if (seller.isPresent()) {
            return seller.get();
        }
        throw new NotFoundException("Seller not found with ID: " + id);
    }
}
