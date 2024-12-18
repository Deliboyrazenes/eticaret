package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.user.Admin;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.entity.user.Role;
import com.deliboyraz.eticaret.entity.user.Seller;
import com.deliboyraz.eticaret.enums.Genders;
import com.deliboyraz.eticaret.repository.user.AdminRepository;
import com.deliboyraz.eticaret.repository.user.CustomerRepository;
import com.deliboyraz.eticaret.repository.user.RoleRepository;
import com.deliboyraz.eticaret.repository.user.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;

@Service
public class AuthenticationService {

    private AdminRepository adminRepository;
    private CustomerRepository customerRepository;
    private SellerRepository sellerRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationService(AdminRepository adminRepository, CustomerRepository customerRepository, SellerRepository sellerRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Admin registerAdmin(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role role = roleRepository.findByAuthority("ADMIN").get();

        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(encodedPassword);
        admin.setAuthority(role);
        return adminRepository.save(admin);
    }

    @Transactional
    public Customer registerCustomer(String firstName, String lastName, Genders gender, String phone, LocalDate dateOfBirth, String email, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role role = roleRepository.findByAuthority("CUSTOMER").get();

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setGender(gender);
        customer.setPhone(phone);
        customer.setDateOfBirth(dateOfBirth);
        customer.setEmail(email);

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        customer.setAge(age);

        customer.setPassword(encodedPassword);
        customer.setAuthority(role);

        return customerRepository.save(customer);
    }

    @Transactional
    public Seller registerSeller(String name, String phone, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Role role = roleRepository.findByAuthority("SELLER").get();

        Seller seller = new Seller();
        seller.setName(name);
        seller.setPhone(phone);
        seller.setPassword(encodedPassword);
        seller.setAuthority(role);

        return sellerRepository.save(seller);
    }
}
