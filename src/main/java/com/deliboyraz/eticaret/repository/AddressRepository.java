package com.deliboyraz.eticaret.repository;

import com.deliboyraz.eticaret.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndCustomerId(Long id, Long customerId);
}
