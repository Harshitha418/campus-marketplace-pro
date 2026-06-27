package campusmarketplace.service;

import campusmarketplace.dto.AdminDashboardResponse;
import campusmarketplace.repository.OrderRepository;
import campusmarketplace.repository.ProductRepository;
import campusmarketplace.repository.ReviewRepository;
import campusmarketplace.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public AdminService(
            UserRepository userRepository,
            ProductRepository productRepository,
            OrderRepository orderRepository,
            ReviewRepository reviewRepository) {

        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse response = new AdminDashboardResponse();

        response.setTotalUsers(userRepository.count());
        response.setTotalProducts(productRepository.count());
        response.setTotalOrders(orderRepository.count());
        response.setTotalReviews(reviewRepository.count());

        response.setTotalRevenue(
                orderRepository.getTotalRevenue());

        response.setAverageRating(
                reviewRepository.getAverageRating());

        response.setTotalQuantitySold(
                orderRepository.getTotalQuantitySold());

        return response;
    }
}