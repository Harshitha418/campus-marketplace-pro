package campusmarketplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Which order (receipt) this line belongs to.
    private Long orderId;

    // Which product, and how many.
    private UUID productId;
    private Integer quantity;

    // Snapshot of the price at purchase time, so later price changes
    // don't rewrite history on old orders.
    private Double priceAtPurchase;

    // Each item has its own status (a laptop can ship while notes are still packed).
    private String status;
}