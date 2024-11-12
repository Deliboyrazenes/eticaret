package com.deliboyraz.eticaret.mapper;

import com.deliboyraz.eticaret.dto.AddressDTO;
import com.deliboyraz.eticaret.dto.OrderDTO;
import com.deliboyraz.eticaret.entity.Address;
import com.deliboyraz.eticaret.entity.Order;

import java.util.stream.Collectors;

public class AddressMapper {

    public static AddressDTO entityToDto(Address address) {
        if (address == null) {
            return null;
        }

        return new AddressDTO(
                address.getId(),
                address.getCity(),
                address.getState()
        );
    }
    public static Address dtoToEntity(AddressDTO addressDTO){
        if (addressDTO == null){
            return null;
        }

        Address address = new Address();
        address.setId(addressDTO.id());
        address.setCity(address.getCity());
        address.setState(address.getState());
        return  address;

    }
}
