package com.deliboyraz.eticaret.mapper.user;

import com.deliboyraz.eticaret.dto.user.CustomerDTO;
import com.deliboyraz.eticaret.entity.user.Customer;

public class CustomerMapper {
    public static CustomerDTO entityToDto(Customer customer) {
        if (customer == null) {
            return null;

        }

        return new CustomerDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getGender(),
                customer.getAge()
        );


    }

    public static Customer dtoToEntity(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;

        }

        Customer customer = new Customer();
        customer.setId(customerDTO.id());
        customer.setFirstName(customerDTO.firsName());
        customer.setLastName(customerDTO.lastName());
        customer.setGender(customerDTO.genders());
        customer.setAge(customerDTO.age());
        return customer;
    }



}
