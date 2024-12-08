package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // İç sınıf olarak PasswordUpdateRequest'i tanımlıyoruz
    public static class PasswordUpdateRequest {
        private String currentPassword;
        private String newPassword;

        // Getter ve Setter metodları
        public String getCurrentPassword() {
            return currentPassword;
        }

        public void setCurrentPassword(String currentPassword) {
            this.currentPassword = currentPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<Customer> getProfile(Authentication authentication) {
        try {
            Customer customer = customerService.findCustomerByEmail(authentication.getName());
            customer.setPassword(null); // Güvenlik için şifreyi null yapıyoruz
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Customer> updateProfile(
            @RequestBody Customer updateCustomer,
            Authentication authentication) {
        try {
            Customer existingCustomer = customerService.findCustomerByEmail(authentication.getName());

            existingCustomer.setFirstName(updateCustomer.getFirstName());
            existingCustomer.setLastName(updateCustomer.getLastName());
            existingCustomer.setPhone(updateCustomer.getPhone());
            existingCustomer.setGender(updateCustomer.getGender());
            existingCustomer.setDateOfBirth(updateCustomer.getDateOfBirth());

            Customer updatedCustomer = customerService.updateCustomer(existingCustomer);
            updatedCustomer.setPassword(null);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/profile/password")
    public ResponseEntity<?> updatePassword(
            @RequestBody PasswordUpdateRequest passwordRequest,
            Authentication authentication) {
        try {
            customerService.updatePassword(
                    authentication.getName(),
                    passwordRequest.getCurrentPassword(),
                    passwordRequest.getNewPassword()
            );
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Geçersiz şifre");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}