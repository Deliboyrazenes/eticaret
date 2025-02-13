package com.deliboyraz.eticaret.service;

import com.deliboyraz.eticaret.controller.BaseController;
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
import java.util.stream.Collectors;


@Service
public class OrderService extends BaseController {
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

//        if (customer.getAddresses() == null || customer.getAddresses().isEmpty()) {
//            throw new NotFoundException("Address is required to place an order");
//        }

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

    @Transactional(readOnly = true)
    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersBySeller(Long sellerId) {
        if (sellerId == null) {
            return new ArrayList<>();
        }
        List<Order> orders = orderRepository.findOrdersBySellerId(sellerId);

        // Her siparişte sadece satıcıya ait ürünleri filtrele
        orders.forEach(order -> {
            List<OrderItem> filteredItems = order.getOrderItems().stream()
                    .filter(item -> item.getProduct().getSeller().getId().equals(sellerId))
                    .collect(Collectors.toList());
            order.setOrderItems(filteredItems);
        });

        return orders;
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        // Durum geçiş mantığı
        switch (order.getStatus()) {
            case PENDING:
                if (newStatus == Status.SHIPPED) {
                    // Satıcıların onaylama durumunu kontrol et
                    List<OrderItem> approvedItems = order.getOrderItems().stream()
                            .filter(item -> item.getProduct().getSeller().getId().equals(getAuthenticatedUserId()))
                            .collect(Collectors.toList());

                    approvedItems.forEach(item -> item.setQuantity(0)); // Onaylanan ürünleri geçici olarak 0 olarak işaretle

                    // Tüm satıcılar onayladı mı kontrol et
                    boolean allItemsApproved = order.getOrderItems().stream()
                            .allMatch(item -> item.getQuantity() == 0);

                    if (allItemsApproved) {
                        order.setStatus(Status.SHIPPED);
                        order.getOrderItems().forEach(item -> item.setQuantity(1)); // Onaylanan ürünleri tekrar 1 olarak işaretle
                    }
                } else if (newStatus == Status.CANCELLED) {
                    order.setStatus(newStatus);
                } else {
                    throw new IllegalStateException("Beklemedeki siparişler sadece kargoya verilebilir veya iptal edilebilir");
                }
                break;
            case SHIPPED:
                if (newStatus == Status.DELIVERED || newStatus == Status.CANCELLED) {
                    order.setStatus(newStatus);
                } else {
                    throw new IllegalStateException("Kargodaki siparişler sadece teslim edildi olarak işaretlenebilir veya iptal edilebilir");
                }
                break;
            case DELIVERED:
            case CANCELLED:
                throw new IllegalStateException("Bu sipariş durumu değiştirilemez");
            default:
                throw new IllegalStateException("Geçersiz sipariş durumu");
        }
        return orderRepository.save(order);
    }
}
