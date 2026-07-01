package campusmarketplace.controller;

import campusmarketplace.service.WishlistService;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.dto.WishlistResponse;
import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(
            WishlistService wishlistService) {

        this.wishlistService = wishlistService;
    }

    @PostMapping("/{productId}")
    public String addToWishlist(
            @PathVariable Long productId,
            @RequestParam String email) {

        return wishlistService
                .addToWishlist(productId, email);
    }

    @GetMapping
    public List<WishlistResponse> getWishlist(
            @RequestParam String email) {

        return wishlistService
                .getWishlist(email);
    }

    @DeleteMapping("/{id}")
    public String removeWishlist(
            @PathVariable Long id) {

        return wishlistService
                .removeFromWishlist(id);
    }
}