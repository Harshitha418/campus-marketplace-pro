package campusmarketplace.service;

import campusmarketplace.dto.CreateReviewRequest;
import campusmarketplace.entity.Review;
import campusmarketplace.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(
            ReviewRepository reviewRepository) {

        this.reviewRepository = reviewRepository;
    }

    public String addReview(
            UUID productId,
            String userEmail,
            CreateReviewRequest request) {

        if (request.getRating() == null
                || request.getRating() < 1
                || request.getRating() > 5) {

            return "Rating must be between 1 and 5";
        }

        Review review = new Review();

        review.setProductId(productId);
        review.setUserEmail(userEmail);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        return "Review added successfully";
    }

    public List<Review> getReviews(
            UUID productId) {

        return reviewRepository.findByProductId(
                productId);
    }

    /**
     * Returns a map of productId -> { average, count } for ALL products,
     * built from a single grouped query. Avoids the frontend firing one
     * request per product card (the N+1 problem).
     */
    public Map<String, Map<String, Object>> getRatingSummary() {

        List<Object[]> rows = reviewRepository.getRatingSummary();

        Map<String, Map<String, Object>> summary = new HashMap<>();

        for (Object[] row : rows) {

            UUID productId = (UUID) row[0];
            Double average = (Double) row[1];
            Long count = (Long) row[2];

            Map<String, Object> stats = new HashMap<>();
            stats.put("average", Math.round(average * 10.0) / 10.0); // 1 decimal
            stats.put("count", count);

            summary.put(productId.toString(), stats);
        }

        return summary;
    }
}