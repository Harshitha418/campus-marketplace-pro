package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDashboardResponse {

    private Long totalUsers;

    private Long totalProducts;

    private Long totalOrders;

    private Long totalReviews;

    private Double totalRevenue;

    private Double averageRating;

    private Integer totalQuantitySold;
}