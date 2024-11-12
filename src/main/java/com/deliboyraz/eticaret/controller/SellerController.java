package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.ProductDTO;
import com.deliboyraz.eticaret.entity.Product;
import com.deliboyraz.eticaret.mapper.ProductMapper;
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
public class SellerController extends BaseController{

    private SellerService sellerService;

    @Autowired
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }


    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findProductsBySeller() {
        Long authenticatedSellerId = getAuthenticatedUserId();
        List<Product> products = sellerService.findProductsBySeller(authenticatedSellerId);
        List<ProductDTO> productDTOS = products.stream()
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOS);
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
