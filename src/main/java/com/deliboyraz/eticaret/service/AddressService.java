package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.Address;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerService customerService;

    @Autowired
    public AddressService(AddressRepository addressRepository, CustomerService customerService) {
        this.addressRepository = addressRepository;
        this.customerService = customerService;
    }

    public Address findAddressById(Long addressId) throws NotFoundException {
        Optional<Address> address = addressRepository.findById(addressId);
        if (address.isPresent()) {
            return address.get();
        }
        throw new NotFoundException("Address not found with ID: " + addressId);
    }

    public List<Address> findAddressesByCustomerId(Long customerId) throws NotFoundException {
        Customer customer = customerService.findById(customerId);
        List<Address> addresses = customer.getAddresses();
        return addresses;
    }

    @Transactional
    public void deleteAddressByCustomer(Long customerId, Long addressId) throws NotFoundException {
        Customer customer = customerService.findById(customerId);
        Address address = findAddressById(addressId);

        if (!address.getCustomer().getId().equals(customerId)) {
            throw new NotFoundException("Address does not belong to the customer.");
        }

        customer.getAddresses().remove(address);
        addressRepository.delete(address);
    }

    public List<Address> findAllAddress() {
        return addressRepository.findAll();
    }

    @Transactional
    public Address addAddress(Address address, Long customerId) {
        Customer customer = customerService.findById(customerId);
        address.setCustomer(customer);
        Address savedAddress = addressRepository.save(address);
        customer.getAddresses().add(savedAddress);
        return savedAddress;
    }

    @Transactional
    public void deleteAddressById(Long id) throws NotFoundException {
        if (!addressRepository.existsById(id)) {
            throw new NotFoundException("Address with ID: " + id + " does not exist.");
        }
        addressRepository.deleteById(id);
    }

    @Transactional
    public Address updateAddress(Address address) {
        return addressRepository.save(address);
    }
}