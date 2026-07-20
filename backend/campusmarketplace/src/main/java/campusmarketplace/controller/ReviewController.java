package campusmarketplace.controller;

import campusmarketplace.dto.CreateReviewRequest;
import campusmarketplace.entity.Review;
import campusmarketplace.service.ReviewService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.UUID;
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
            @PathVariable UUID productId,
            @RequestBody CreateReviewRequest request,
            Authentication authentication) {

        return reviewService.addReview(
                productId,
                authentication.getName(),
                request);
    }

    @GetMapping("/{productId}")
    public List<Review> getReviews(
            @PathVariable UUID productId) {

        return reviewService.getReviews(
                productId);
    }

    @GetMapping("/summary")
    public java.util.Map<String, java.util.Map<String, Object>> getRatingSummary() {

        return reviewService.getRatingSummary();
    }
}