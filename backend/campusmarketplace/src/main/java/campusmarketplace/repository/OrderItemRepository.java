package campusmarketplace.repository;

import campusmarketplace.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Long> {

    // All line items belonging to one order (receipt).
    List<OrderItem> findByOrderId(Long orderId);
}