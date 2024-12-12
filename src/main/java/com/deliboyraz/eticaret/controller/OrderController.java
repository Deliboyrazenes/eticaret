package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.CheckoutDTO;
import com.deliboyraz.eticaret.dto.OrderDTO;
import com.deliboyraz.eticaret.entity.Order;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.enums.PaymentMethods;
import com.deliboyraz.eticaret.mapper.OrderMapper;
import com.deliboyraz.eticaret.service.CustomerService;
import com.deliboyraz.eticaret.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController extends BaseController {
    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestParam PaymentMethods paymentMethod) throws Exception {
        Long authenticatedUserId = getAuthenticatedUserId(); // KİMLİĞİ DOĞRULA

        Customer customer = customerService.findById(authenticatedUserId);

        Order order = orderService.createOrder(customer.getId(), paymentMethod);
        return new ResponseEntity<>(OrderMapper.entityToDto(order), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        Long authenticatedUserId = getAuthenticatedUserId(); // KİMLİĞİ DOĞRULA
        List<Order> orders = orderService.getOrdersByCustomerId(authenticatedUserId);
        return ResponseEntity.ok(orders.stream().map(OrderMapper::entityToDto).toList());
    }
}



