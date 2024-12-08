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

    // MÜŞTERİ İDSİNE GÖRE SEPETE ÜRÜN EKLE
    @Transactional
    public Cart addProductToCart(Long customerId, Long productId) {
        // Sepeti bul veya oluştur
        Cart cart = findOrCreateCartForCustomer(customerId);

        // Ürünü bul
        Product product = productService.findProductById(productId);

        // Stok kontrolü
        if (product.getStock() < 1) {
            throw new InsufficientStockException("This product is out of stock: " + product.getName());
        }

        // Ürünü sepete ekle
        cart.getProducts().add(product);

        // Stoktan düş
        product.setStock(product.getStock() - 1);

        // Ara toplam ve genel toplamı hesapla
        BigDecimal itemTotal = cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setItemTotal(itemTotal);
        cart.setGrandTotal(itemTotal);

        // Sepeti kaydet
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateProductQuantity(Long customerId, Long productId, int newQuantity) {
        // Sepeti bul veya oluştur
        Cart cart = findOrCreateCartForCustomer(customerId);

        // Ürünü bul
        Product product = productService.findProductById(productId);

        // Sepette ürün var mı kontrol et
        long currentQuantity = cart.getProducts().stream()
                .filter(p -> p.getId()== productId)
                .count();

        if (currentQuantity == 0) {
            throw new NotFoundException("Product not found in cart: " + product.getName());
        }

        // Stok kontrolü
        int stockAdjustment = (int) (currentQuantity - newQuantity);
        if (product.getStock() + stockAdjustment < 0) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }

        // Yeni miktara göre ürünleri ekle veya çıkar
        if (newQuantity > currentQuantity) {
            // Ürün miktarını artır
            for (int i = 0; i < newQuantity - currentQuantity; i++) {
                cart.getProducts().add(product);
                product.setStock(product.getStock() - 1);
            }
        } else if (newQuantity < currentQuantity) {
            // Ürün miktarını azalt
            for (int i = 0; i < currentQuantity - newQuantity; i++) {
                cart.getProducts().remove(product);
                product.setStock(product.getStock() + 1);
            }
        }

        // Ara toplam ve genel toplamı yeniden hesapla
        BigDecimal itemTotal = cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setItemTotal(itemTotal);
        cart.setGrandTotal(itemTotal);

        // Sepeti kaydet
        return cartRepository.save(cart);
    }


    @Transactional
    public Cart removeProductFromCart(Long customerId, Long productId) {
        // Sepeti bul veya oluştur
        Cart cart = findOrCreateCartForCustomer(customerId);

        // Ürünü bul
        Product product = productService.findProductById(productId);

        // Sepette ürün var mı kontrol et
        long currentQuantity = cart.getProducts().stream()
                .filter(p -> p.getId()== productId)
                .count();

        if (currentQuantity == 0) {
            throw new NotFoundException("Product not found in cart: " + product.getName());
        }

        // Sepetten ürünü çıkar ve stokları geri ekle
        cart.getProducts().removeIf(p -> p.getId()== productId);
        product.setStock(product.getStock() + (int) currentQuantity);

        // Ara toplam ve genel toplamı yeniden hesapla
        BigDecimal itemTotal = cart.getProducts().stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setItemTotal(itemTotal);
        cart.setGrandTotal(itemTotal);

        // Sepeti kaydet
        return cartRepository.save(cart);
    }
}
