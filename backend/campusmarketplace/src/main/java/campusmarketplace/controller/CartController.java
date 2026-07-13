package campusmarketplace.controller;

import campusmarketplace.service.CartService;
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

    @PostMapping("/{productId}")
    public String addToCart(
            @PathVariable UUID productId,
            @RequestParam String email) {

        return cartService
                .addToCart(productId, email);
    }

    @GetMapping
    public List<CartResponse> getCart(
            @RequestParam String email) {

        return cartService
                .getCart(email);
    }

    @DeleteMapping("/{id}")
    public String removeCart(
            @PathVariable Long id) {

        return cartService
                .removeFromCart(id);
    }
}