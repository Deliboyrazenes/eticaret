    package com.deliboyraz.eticaret.service;

    import com.deliboyraz.eticaret.entity.Product;
    import com.deliboyraz.eticaret.entity.user.Seller;
    import com.deliboyraz.eticaret.exceptions.NotFoundException;
    import com.deliboyraz.eticaret.repository.user.SellerRepository;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class SellerService {
        private SellerRepository sellerRepository;

        public SellerService(SellerRepository sellerRepository) {
            this.sellerRepository = sellerRepository;

        }

        public Seller findSellerByPhone(String phone) throws NotFoundException {
            Optional<Seller> seller = sellerRepository.findSellerByPhone(phone);
            if (seller.isPresent()) {
                return seller.get();
            }
            throw new NotFoundException("Seller not found with phone: " + phone);
    }



        public Seller findById(Long id) throws NotFoundException{
            Optional<Seller> seller = sellerRepository.findById(id);
            if (seller.isPresent()) {
                return seller.get();
            }
            throw new NotFoundException("Seller not found with ID: " + id);
        }

        public List<Product> findProductsBySeller(Long id) {
            Seller seller = findById(id);
            return seller.getProducts();
    }


    }
