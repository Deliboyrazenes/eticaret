package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.CustomerLoginRequest;
import com.deliboyraz.eticaret.dto.SellerLoginRequest;
import com.deliboyraz.eticaret.dto.user.AdminDTO;
import com.deliboyraz.eticaret.dto.user.CustomerDTO;
import com.deliboyraz.eticaret.dto.user.SellerDTO;
import com.deliboyraz.eticaret.entity.user.Admin;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.entity.user.Seller;
import com.deliboyraz.eticaret.mapper.user.AdminMapper;
import com.deliboyraz.eticaret.mapper.user.CustomerMapper;
import com.deliboyraz.eticaret.mapper.user.SellerMapper;
import com.deliboyraz.eticaret.service.AuthenticationService;
import com.deliboyraz.eticaret.service.CustomerService;
import com.deliboyraz.eticaret.service.JwtUtil;
import com.deliboyraz.eticaret.service.SellerService;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SellerService sellerService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService,
                                    JwtUtil jwtUtil,
                                    CustomerService customerService,
                                    BCryptPasswordEncoder bCryptPasswordEncoder,
                                    SellerService sellerService) {
        this.authenticationService = authenticationService;
        this.jwtUtil= jwtUtil;
        this.customerService = customerService;
        this.passwordEncoder = bCryptPasswordEncoder;
        this.sellerService = sellerService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CustomerLoginRequest customerLoginRequest) {
        try {
            System.out.println("Giriş isteği alındı: " + customerLoginRequest.getEmail());

            Customer customer = customerService.findCustomerByEmail(customerLoginRequest.getEmail().toLowerCase());
            System.out.println("Kullanıcı bulundu: " + customer.getEmail());

            if (passwordEncoder.matches(customerLoginRequest.getPassword(), customer.getPassword())) {
                System.out.println("Şifre doğrulandı.");
                String token = jwtUtil.generateToken(customer.getEmail(), String.valueOf(customer.getAuthority()));
                System.out.println("Token generated: " + token); // Debug için

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("userType", String.valueOf(customer.getAuthority()));
                response.put("firstName", customer.getFirstName());
                response.put("lastName", customer.getLastName());
                response.put("email", customer.getEmail());

                System.out.println("Response data: " + response); // Debug için
                return ResponseEntity.ok(response);
            } else {
                System.out.println("Hatalı şifre!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı şifre!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sunucu hatası!");
        }
    }

    @PostMapping("/sellerlogin")
    public ResponseEntity<?> sellerLogin(@RequestBody SellerLoginRequest sellerLoginRequest) {
        try {
            System.out.println("Giriş isteği alındı: " + sellerLoginRequest.getPhone());

            Seller seller = sellerService.findSellerByPhone(sellerLoginRequest.getPhone());
            System.out.println("Kullanıcı bulundu: " + seller.getPhone());

            if (passwordEncoder.matches(sellerLoginRequest.getPassword(), seller.getPassword())) {
                System.out.println("Şifre doğrulandı.");
                // Telefon numarasını token'a ekle
                String token = jwtUtil.generateToken(seller.getPhone(), String.valueOf(seller.getAuthority()));

                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("userType", String.valueOf(seller.getAuthority()));
                response.put("sellerId", String.valueOf(seller.getId()));
                response.put("name", seller.getName());
                response.put("phone", seller.getPhone());

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı şifre!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Sunucu hatası!");
        }
    }



    @PostMapping("/register/admin")
    public ResponseEntity<AdminDTO> register(@RequestBody Admin admin) {
        try {
            Admin savedAdmin = authenticationService.registerAdmin(
                    admin.getUsername(),
                    admin.getPassword());
            AdminDTO adminDTO = AdminMapper.entityToDto(savedAdmin);

            return ResponseEntity.status(HttpStatus.CREATED).body(adminDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/register/customer")
    public ResponseEntity<CustomerDTO> registerCustomer(@RequestBody Customer customer) {
        try {
            Customer savedCustomer = authenticationService.registerCustomer(
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getGender(),
                    customer.getPhone(),
                    customer.getDateOfBirth(),
                    customer.getUsername(),
                    customer.getPassword());
            CustomerDTO customerDTO = CustomerMapper.entityToDto(savedCustomer);
            return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/register/seller")
    public ResponseEntity<SellerDTO> registerSeller(@RequestBody Seller seller) {
        try {
            Seller savedSeller = authenticationService.registerSeller(
                    seller.getName(),
                    seller.getUsername(),
                    seller.getPassword());
            SellerDTO sellerDTO = SellerMapper.entityToDto(savedSeller);
            return ResponseEntity.status(HttpStatus.CREATED).body(sellerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
}
}

}
