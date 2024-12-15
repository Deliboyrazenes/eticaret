    package com.deliboyraz.eticaret.repository;

    import com.deliboyraz.eticaret.entity.Order;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface OrderRepository extends JpaRepository<Order, Long> {
        @Query("SELECT DISTINCT o FROM Order o " +
                "JOIN o.orderItems oi " +
                "JOIN oi.product p " +
                "WHERE p.seller.id = :sellerId")
        List<Order> findOrdersBySellerId(@Param("sellerId") Long sellerId);
        List<Order> findByCustomerId(Long customerId);
    }

