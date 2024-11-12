package com.deliboyraz.eticaret.repository.user;

import com.deliboyraz.eticaret.entity.user.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    @Query("SELECT a FROM Admin a WHERE a.username=:username")
    Optional<Admin> findAdminByUsername(String username);

}
