package com.deliboyraz.eticaret.mapper.user;

import com.deliboyraz.eticaret.dto.user.AdminDTO;
import com.deliboyraz.eticaret.entity.user.Admin;

public class AdminMapper {
    public static AdminDTO entityToDto(Admin admin) {
        if (admin == null) {
            return null;
        }
        return new AdminDTO(
                admin.getId(),
                admin.getUsername()
        );
    }

    public static Admin dtoToEntity(AdminDTO adminDTO) {
        if (adminDTO == null) {
            return null;
        }
        Admin admin = new Admin();
        admin.setId(adminDTO.id());
        admin.setUsername(adminDTO.username());
        return admin;
}
}
