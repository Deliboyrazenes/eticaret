package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.CartDTO;
import com.deliboyraz.eticaret.dto.UpdateQuantityRequest;
import com.deliboyraz.eticaret.entity.Cart;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.mapper.CartMapper;
import com.deliboyraz.eticaret.service.CartService;
import com.deliboyraz.eticaret.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        Long customerId = getAuthenticatedUserId();
        Customer customer = customerService.findById(customerId);
        Cart cart = cartService.findOrCreateCartForCustomer(customerId);
        return ResponseEntity.ok(CartMapper.entityToDto(cart));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<CartDTO> updateProductQuantity(
            @PathVariable Long productId,
            @RequestBody UpdateQuantityRequest request) {
        Long customerId = getAuthenticatedUserId();
        Customer customer = customerService.findById(customerId);

        Cart cart = cartService.updateProductQuantity(customer.getId(), productId, request.getQuantity());
        return ResponseEntity.ok(CartMapper.entityToDto(cart));
    }


}
