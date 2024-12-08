package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.Order;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.repository.AddressRepository;
import com.deliboyraz.eticaret.repository.user.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public CustomerService(CustomerRepository customerRepository, AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
    }

    public Customer findById(Long id) throws NotFoundException {
        return customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer with ID: " + id + " not found."));
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findCustomerByEmail(String email) throws NotFoundException {
        return customerRepository.findCustomerByEmail(email).orElseThrow(() -> new NotFoundException("Customer with email: " + email + " not found."));
    }

    @Transactional
    public void deleteCustomerById(Long id) throws NotFoundException {
        if (!customerRepository.existsById(id)) {
            throw new NotFoundException("Customer with ID: " + id + " not found.");
        }
        customerRepository.deleteById(id);
    }

    public List<Order> findOrdersByCustomerId(Long customerId) {
        Customer customer = findById(customerId);
        return customer.getOrders();
    }


    @Transactional
    public Customer updateCustomer(Customer customer) {
        Customer existingCustomer = findCustomerByEmail(customer.getEmail());

        // Email değiştirilmeyecek
        customer.setEmail(existingCustomer.getEmail());

        // Şifre değiştirilmeyecek
        customer.setPassword(existingCustomer.getPassword());

        return customerRepository.save(customer);
    }

    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword) {
        Customer customer = findCustomerByEmail(email);

        if (!passwordEncoder.matches(currentPassword, customer.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Şifre uzunluğu kontrolü
        if (newPassword.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        customer.setPassword(passwordEncoder.encode(newPassword));
        customerRepository.save(customer);
    }

}
