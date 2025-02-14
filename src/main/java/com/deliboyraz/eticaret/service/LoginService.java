package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.user.Admin;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.entity.user.Seller;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.repository.user.AdminRepository;
import com.deliboyraz.eticaret.repository.user.CustomerRepository;
import com.deliboyraz.eticaret.repository.user.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LoginService implements UserDetailsService {

    private AdminRepository adminRepository;
    private CustomerRepository customerRepository;
    private SellerRepository sellerRepository;

    @Autowired
    public LoginService(AdminRepository adminRepository, CustomerRepository customerRepository, SellerRepository sellerRepository) {
        this.adminRepository = adminRepository;
        this.customerRepository = customerRepository;
        this.sellerRepository = sellerRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {

        Admin admin = adminRepository.findAdminByUsername(username).orElse(null);
        if (admin != null) {
            return admin;
        }

        Seller seller = sellerRepository.findSellerByPhone(username).orElse(null);
        if (seller != null) {
            return seller;
        }

        Customer customer = customerRepository.findCustomerByEmail(username).orElse(null);
        if (customer != null) {
            return customer;
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
}
}
