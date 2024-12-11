package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.AddressDTO;
import com.deliboyraz.eticaret.entity.Address;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.mapper.AddressMapper;
import com.deliboyraz.eticaret.service.AddressService;
import com.deliboyraz.eticaret.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/address")
public class AddressController extends BaseController {

    private final AddressService addressService;
    private final CustomerService customerService;

    @Autowired
    public AddressController(AddressService addressService, CustomerService customerService) {
        this.addressService = addressService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<AddressDTO> addAddress(@RequestBody Address address) {
        try {
            Long authenticatedUserId = getAuthenticatedUserId();

            // Address'i customer ID ile birlikte service'e gönder
            Address savedAddress = addressService.addAddress(address, authenticatedUserId);

            AddressDTO addressDTO = AddressMapper.entityToDto(savedAddress);
            return ResponseEntity.ok(addressDTO);
        } catch (Exception e) {
            System.out.println("Adres ekleme hatası: " + e.getMessage()); // Debug için
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getCustomerAddresses() {
        try {
            Long authenticatedUserId = getAuthenticatedUserId();
            List<Address> addresses = addressService.findAddressesByCustomerId(authenticatedUserId);

            List<AddressDTO> addressDTOs = addresses.stream()
                    .map(AddressMapper::entityToDto)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(addressDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable Long addressId) {
        try {
            Long authenticatedUserId = getAuthenticatedUserId();
            addressService.deleteAddressByCustomer(authenticatedUserId, addressId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody Address updatedAddress) {
        try {
            Long authenticatedUserId = getAuthenticatedUserId();
            Address existingAddress = addressService.findAddressById(addressId);

            // Yetki kontrolü
            if (!existingAddress.getCustomer().getId().equals(authenticatedUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Tüm alanları güncelle
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setState(updatedAddress.getState());
            existingAddress.setDescription(updatedAddress.getDescription());
            existingAddress.setPostalCode(updatedAddress.getPostalCode());

            Address savedAddress = addressService.updateAddress(existingAddress);
            return ResponseEntity.ok(AddressMapper.entityToDto(savedAddress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}