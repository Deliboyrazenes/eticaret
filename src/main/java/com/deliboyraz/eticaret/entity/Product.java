package com.deliboyraz.eticaret.entity;

import com.deliboyraz.eticaret.entity.user.Seller;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Product", schema = "public")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull(message = "Ürün adı boş olamaz")
    @Size(min = 2, max = 100, message = "Ürün adı 2 ile 100 karakter arasında olmalıdır")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Fiyat boş olamaz")
    @DecimalMin(value = "0.0", inclusive = false, message = "Fiyat 0'dan büyük olmalıdır")
    @Digits(integer = 10, fraction = 2, message = "Fiyat, en fazla 10 basamaklı tam sayı ve 2 basamaklı ondalık olmalıdır")
    @Column(name = "price")
    private BigDecimal price;

    @NotNull(message = "Stok bilgisi boş olamaz")
    @Min(value = 0, message = "Stok negatif olamaz")
    @Column(name = "stock")
    private Integer stock;

    @NotNull(message = "Marka bilgisi boş olamaz")
    @Size(min = 2, max = 50, message = "Marka adı 2 ile 50 karakter arasında olmalıdır")
    @Column(name = "brand")
    private String brand;

    @NotNull(message = "Kategori boş olamaz")
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull(message = "Satıcı bilgisi boş olamaz")
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToMany(mappedBy = "products")
    private List<Cart> carts;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;
    
    @Column(name = "image_path", length = 1000)
    private String imagePath;

    // `equals` ve `hashCode` metodları
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Aynı referanssa eşittir
        if (o == null || getClass() != o.getClass()) return false; // Farklı sınıflar eşit olamaz
        Product product = (Product) o;
        return id == product.id; // `id` alanına göre eşitlik kontrolü
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // `id` alanına göre hashCode oluştur
    }

    @Transient // DB'de saklanmayacak
    public List<String> getImageList() {
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(imagePath.split(","));
    }

    public void setImageList(List<String> images) {
        if (images == null || images.isEmpty()) {
            this.imagePath = null;
        } else {
            this.imagePath = String.join(",", images);
        }
    }
}