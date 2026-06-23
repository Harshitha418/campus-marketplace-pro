package campusmarketplace.controller;

import campusmarketplace.dto.CreateReviewRequest;
import campusmarketplace.entity.Review;
import campusmarketplace.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(
            ReviewService reviewService) {

        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}")
    public String addReview(
            @PathVariable Long productId,
            @RequestBody CreateReviewRequest request) {

        return reviewService.addReview(
                productId,
                request);
    }

    @GetMapping("/{productId}")
    public List<Review> getReviews(
            @PathVariable Long productId) {

        return reviewService.getReviews(
                productId);
    }
}