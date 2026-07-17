package campusmarketplace.controller;

import campusmarketplace.service.OrderService;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.dto.OrderResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }

    @PostMapping("/place")
    public String placeOrder(
            @RequestParam UUID productId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        return orderService.placeOrder(
                productId,
                authentication.getName(),
                quantity);
    }

    @GetMapping
    public List<OrderResponse> getOrders(
            Authentication authentication) {

        return orderService.getOrders(
                authentication.getName());
    }

    /**
     * Identity comes from the JWT, not a request param — a user
     * must only ever be able to check out their own cart.
     */
    @PostMapping("/checkout")
    public String checkout(Authentication authentication) {

        return orderService.checkout(authentication.getName());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders() {

        return orderService.getAllOrders();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return orderService
                .updateStatus(id, status);
    }
}