package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderSummaryResponse {

    private Long orderId;
    private String userEmail;
    private LocalDateTime createdAt;
    private Double totalAmount;
    private Integer itemCount;
    private String transactionId;
}