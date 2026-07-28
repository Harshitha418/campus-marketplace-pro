package campusmarketplace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Who placed the order.
    private String userEmail;

    // When it was placed — used for "ordered 3 days ago" and newest-first sorting.
    private LocalDateTime createdAt;

    // Total paid for the whole order.
    private Double totalAmount;

    // The Razorpay payment id, so the bill can show a transaction reference.
    private String transactionId;
}