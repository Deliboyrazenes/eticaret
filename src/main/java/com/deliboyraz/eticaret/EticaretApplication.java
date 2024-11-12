package com.deliboyraz.eticaret;

import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.entity.user.Role;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EticaretApplication {

	public static void main(String[] args) {
		SpringApplication.run(EticaretApplication.class, args);
		System.out.println("selam");
		Customer customer = new Customer();
		Role role = new Role(10L,"hey");
		customer.setAuthority(role);
		System.out.println(customer.getAuthority());
	}

}
