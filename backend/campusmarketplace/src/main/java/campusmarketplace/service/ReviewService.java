package campusmarketplace.service;

import campusmarketplace.dto.CreateReviewRequest;
import campusmarketplace.entity.Review;
import campusmarketplace.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(
            ReviewRepository reviewRepository) {

        this.reviewRepository = reviewRepository;
    }

    public String addReview(
            Long productId,
            CreateReviewRequest request) {

        if (request.getRating() < 1
                || request.getRating() > 5) {

            return "Rating must be between 1 and 5";
        }

        Review review = new Review();

        review.setProductId(productId);
        review.setUserEmail(request.getUserEmail());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        reviewRepository.save(review);

        return "Review added successfully";
    }

    public List<Review> getReviews(
            Long productId) {

        return reviewRepository.findByProductId(
                productId);
    }
}