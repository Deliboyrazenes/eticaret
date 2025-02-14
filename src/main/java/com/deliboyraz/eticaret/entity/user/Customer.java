package com.deliboyraz.eticaret.entity.user;

import com.deliboyraz.eticaret.entity.*;
import com.deliboyraz.eticaret.enums.Genders;
import com.fasterxml.jackson.annotation.JsonIgnore;  // Bu import'u ekleyin
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer", schema = "public")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "First name is required")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotNull(message = "Password is required")
    @Size(min = 3, max = 68, message = "Password must be between 3 and 50 characters")
    @Column(name = "password")
    @JsonIgnore  // Şifreyi JSON'da göstermemek için
    private String password;

    @NotNull(message = "Gender is required")
    @Enumerated(value = EnumType.STRING)
    @Column(name = "gender")
    private Genders gender;

    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email")
    private String email;

    @Size(max = 20, message = "Phone number can be up to 20 characters")
    @Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only digits")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Date of birth is required")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Min(value = 1, message = "Age must be greater than 0")
    @Column(name = "age")
    private Integer age;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private Role authority;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore  // Döngüsel referansı önlemek için
    private List<Address> addresses;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Döngüsel referansı önlemek için
    private List<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore  // Döngüsel referansı önlemek için
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore  // Döngüsel referansı önlemek için
    private List<Payment> payments;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore  // Döngüsel referansı önlemek için
    private Cart cart;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
}
}
