package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.entity.Cart;
import com.deliboyraz.eticaret.entity.Order;
import com.deliboyraz.eticaret.entity.OrderItem;
import com.deliboyraz.eticaret.entity.Product;
import com.deliboyraz.eticaret.entity.user.Customer;
import com.deliboyraz.eticaret.enums.PaymentMethods;
import com.deliboyraz.eticaret.enums.Status;
import com.deliboyraz.eticaret.exceptions.NotFoundException;
import com.deliboyraz.eticaret.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;

    public OrderService(OrderRepository orderRepository, CustomerService customerService,
                        ProductService productService, CartService cartService,
                        PaymentService paymentService, OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.orderItemService = orderItemService;
    }

    @Transactional
    public Order createOrder(Long customerId, PaymentMethods paymentMethod) throws NotFoundException {
        // Get customer's cart
        Customer customer = customerService.findById(customerId);
        Cart cart = cartService.findCartByCustomer(customer);

        if (customer.getAddresses() == null || customer.getAddresses().isEmpty()) {
            throw new NotFoundException("Address is required to place an order");
        }

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setAmount(cart.getGrandTotal());
        order.setStatus(Status.PENDING);
        order.setCart(cart);
        order.setCustomer(cart.getCustomer());

        Order savedOrder = orderRepository.save(order);

        createOrderItems(cart, savedOrder);

        updateProductStock(cart);

        paymentService.processPayment(savedOrder, paymentMethod);
        cartService.clearCart(cart);

        return savedOrder;
    }

    @Transactional
    private void updateProductStock(Cart cart) {
        for (Product product : cart.getProducts()) {
            product.setStock(product.getStock() - 1);
            productService.save(product);
        }
    }

    @Transactional
    private void createOrderItems(Cart cart, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Product product : cart.getProducts()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(1);  // Assuming quantity is 1 for simplicity
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItems.add(orderItem);
            orderItemService.save(orderItem);
        }
        order.setOrderItems(orderItems);
        orderRepository.save(order);
    }


}
