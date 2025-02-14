package com.deliboyraz.eticaret.controller;

import com.deliboyraz.eticaret.dto.CheckoutDTO;
import com.deliboyraz.eticaret.dto.OrderDTO;
import com.deliboyraz.eticaret.dto.OrderItemDTO;
import com.deliboyraz.eticaret.entity.Order;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.enums.PaymentMethods;
import com.deliboyraz.eticaret.enums.Status;
import com.deliboyraz.eticaret.mapper.OrderMapper;
import com.deliboyraz.eticaret.service.CustomerService;
import com.deliboyraz.eticaret.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersBySeller(@PathVariable Long sellerId) {
        try {
            List<Order> orders = orderService.getOrdersBySeller(sellerId);

            if (orders.isEmpty()) {
                return ResponseEntity.ok(new ArrayList<>());
            }

            List<OrderDTO> orderDTOs = orders.stream()
                    .map(OrderMapper::entityToDto)
                    .collect(Collectors.toList());

            orderDTOs.forEach(orderDTO -> {
                List<OrderItemDTO> filteredItems = orderDTO.orderItems().stream()
                        .filter(item -> item.product().seller().id().equals(sellerId))
                        .collect(Collectors.toList());
                orderDTO.orderItems().clear();
                orderDTO.orderItems().addAll(filteredItems);
            });

            return ResponseEntity.ok(orderDTOs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/seller/update-status/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Status newStatus) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, newStatus);
            return ResponseEntity.ok(OrderMapper.entityToDto(updatedOrder));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}



