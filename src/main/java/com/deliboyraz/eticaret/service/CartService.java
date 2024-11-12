package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.Cart;
import com.deliboyraz.eticaret.entity.Product;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.exceptions.InsufficientStockException;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CartService {
    private CartRepository cartRepository;
    private CustomerService customerService;
    private ProductService productService;

    @Autowired
    public CartService(CartRepository cartRepository, CustomerService customerService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.customerService = customerService;
        this.productService = productService;
}

    //MÜŞTERİNİN SEPETİ YOKSA OLUŞTUR VARSA VAR OLANI DÖN
    @Transactional
    public Cart findOrCreateCartForCustomer(Long customerId) {
        Customer customer = customerService.findById(customerId);

        return cartRepository.findByCustomer(customer)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setCustomer(customer);
                    cart.setItemTotal(BigDecimal.valueOf(0.0));
                    cart.setGrandTotal(BigDecimal.valueOf(0.0));
                    return cartRepository.save(cart);
                });
    }

    //MÜŞTERİ İDSİNE GÖRE SEPETE ÜRÜN EKLE
    @Transactional
    public Cart addProductToCart(Long customerId, Long productId) throws InsufficientStockException {
        Cart cart = findOrCreateCartForCustomer(customerId);

        Product product = productService.findProductById(productId);

        //STOK KONTROLÜ
        if (product.getStock() < 1) {
            throw new InsufficientStockException("This products is out of stock " + product.getName());
        }

        cart.getProducts().add(product);

        BigDecimal itemTotal = cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setItemTotal(itemTotal);
        cart.setGrandTotal(itemTotal);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeProductFromCart(Long customerId, Long productId) {
        Cart cart = findOrCreateCartForCustomer(customerId);

        Product product = productService.findProductById(productId);

        if (!cart.getProducts().contains(product)) {
            throw new NotFoundException("Product not found in cart: " + product.getName());
        }

        cart.getProducts().remove(product);

        BigDecimal itemTotal = cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setItemTotal(itemTotal);
        cart.setGrandTotal(itemTotal);
        return cartRepository.save(cart);
}
}
