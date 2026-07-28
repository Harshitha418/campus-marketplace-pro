package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDetailResponse {

    private Long orderId;
    private String userEmail;
    private LocalDateTime createdAt;
    private Double totalAmount;
    private String transactionId;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private Long itemId;
        private UUID productId;
        private String title;
        private String imageUrl;
        private Integer quantity;
        private Double priceAtPurchase;
        private String status;
    }
}