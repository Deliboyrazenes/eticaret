package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.ProductDTO;
import com.deliboyraz.eticaret.dto.user.SellerDTO;
import com.deliboyraz.eticaret.entity.Product;
import com.deliboyraz.eticaret.entity.user.Seller;
import com.deliboyraz.eticaret.mapper.ProductMapper;
import com.deliboyraz.eticaret.mapper.user.SellerMapper;
import com.deliboyraz.eticaret.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/seller")
public class SellerController extends BaseController {

    private final SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @GetMapping("/info")
    public ResponseEntity<SellerDTO> getSellerInfo() {
        try {
            Long sellerId = getAuthenticatedUserId();
            Seller seller = sellerService.findById(sellerId);
            return ResponseEntity.ok(SellerMapper.entityToDto(seller));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findProductsBySeller() {
        try {
            Long sellerId = getAuthenticatedUserId();
            List<Product> products = sellerService.findProductsBySeller(sellerId);
            List<ProductDTO> productDTOS = products.stream()
                    .map(ProductMapper::entityToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsBySellerId(@PathVariable Long id) {
        List<Product> products = sellerService.findProductsBySeller(id);
        List<ProductDTO> productDTOS = products.stream()
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOS);
    }



}
