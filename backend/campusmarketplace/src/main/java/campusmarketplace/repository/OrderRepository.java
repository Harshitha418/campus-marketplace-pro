package campusmarketplace.repository;

import campusmarketplace.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface OrderRepository
                extends JpaRepository<OrderEntity, Long> {

        List<OrderEntity> findByUserEmail(
                        String userEmail);

        long count();

        @Query("""
                        SELECT COALESCE(SUM(o.quantity * p.price),0)
                        FROM OrderEntity o
                        JOIN Product p
                        ON o.productId = p.id
                                """)
        Double getTotalRevenue();

        @Query("""
                        SELECT COALESCE(SUM(o.quantity),0)
                        FROM OrderEntity o
                        """)
        Integer getTotalQuantitySold();
}