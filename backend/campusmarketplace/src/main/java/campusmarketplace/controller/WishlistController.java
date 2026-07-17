package campusmarketplace.controller;

import campusmarketplace.service.WishlistService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.dto.WishlistResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(
            WishlistService wishlistService) {

        this.wishlistService = wishlistService;
    }

    /**
     * Identity comes from the validated JWT, never from a client-supplied
     * param — otherwise anyone could read or modify another user's wishlist.
     */
    @PostMapping("/{productId}")
    public String addToWishlist(
            @PathVariable UUID productId,
            Authentication authentication) {

        return wishlistService
                .addToWishlist(productId, authentication.getName());
    }

    @GetMapping
    public List<WishlistResponse> getWishlist(
            Authentication authentication) {

        return wishlistService
                .getWishlist(authentication.getName());
    }

    @DeleteMapping("/{id}")
    public String removeWishlist(
            @PathVariable Long id) {

        return wishlistService
                .removeFromWishlist(id);
    }
}