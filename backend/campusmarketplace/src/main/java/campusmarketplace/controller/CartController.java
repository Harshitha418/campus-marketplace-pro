package campusmarketplace.controller;

import campusmarketplace.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.dto.CartResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(
            CartService cartService) {

        this.cartService = cartService;
    }

    /**
     * Identity comes from the validated JWT, never from a client-supplied
     * param — otherwise anyone could read or modify another user's cart.
     */
    @PostMapping("/{productId}")
    public String addToCart(
            @PathVariable UUID productId,
            Authentication authentication) {

        return cartService
                .addToCart(productId, authentication.getName());
    }

    @GetMapping
    public List<CartResponse> getCart(
            Authentication authentication) {

        return cartService
                .getCart(authentication.getName());
    }

    @PutMapping("/{id}")
    public String updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            Authentication authentication) {

        return cartService
                .updateQuantity(id, quantity, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public String removeCart(
            @PathVariable Long id,
            Authentication authentication) {

        return cartService
                .removeFromCart(id, authentication.getName());
    }
}