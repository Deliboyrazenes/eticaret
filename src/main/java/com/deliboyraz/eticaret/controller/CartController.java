package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.CartDTO;
import com.deliboyraz.eticaret.entity.Cart;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.mapper.CartMapper;
import com.deliboyraz.eticaret.service.CartService;
import com.deliboyraz.eticaret.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController extends BaseController{
    private CartService cartService;
    private CustomerService customerService;

    public CartController(CartService cartService, CustomerService customerService) {
        this.cartService = cartService;
        this.customerService = customerService;
    }

    @PostMapping("/{productId}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId) {
        Long customerId = getAuthenticatedUserId();
        Customer customer = customerService.findById(customerId);

        Cart cart = cartService.addProductToCart(customer.getId(), productId);
        return ResponseEntity.ok(CartMapper.entityToDto(cart));
    }

    @PostMapping("/remove/{productId}")
    public ResponseEntity<CartDTO> removeProductFromCart(@PathVariable Long productId) {
        Long customerId = getAuthenticatedUserId();
        Customer customer = customerService.findById(customerId);

        Cart cart = cartService.removeProductFromCart(customer.getId(), productId);
        return ResponseEntity.ok(CartMapper.entityToDto(cart));

}
}
