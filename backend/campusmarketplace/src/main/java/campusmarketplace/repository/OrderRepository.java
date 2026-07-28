package campusmarketplace.repository;

import campusmarketplace.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository
                extends JpaRepository<OrderEntity, Long> {

        // A user's orders, newest first.
        List<OrderEntity> findByUserEmailOrderByCreatedAtDesc(String userEmail);

        // All orders, newest first (admin view).
        List<OrderEntity> findAllByOrderByCreatedAtDesc();

        long count();

        // Revenue is now the sum of each order's total.
        @Query("""
                        SELECT COALESCE(SUM(o.totalAmount), 0)
                        FROM OrderEntity o
                        """)
        Double getTotalRevenue();

        // Total units sold now comes from the order items.
        @Query("""
                        SELECT COALESCE(SUM(i.quantity), 0)
                        FROM OrderItem i
                        """)
        Integer getTotalQuantitySold();
}