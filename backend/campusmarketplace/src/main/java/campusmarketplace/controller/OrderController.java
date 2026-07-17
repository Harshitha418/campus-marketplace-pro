package campusmarketplace.controller;

import campusmarketplace.service.OrderService;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.dto.OrderResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

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
            @RequestParam String email,
            @RequestParam Integer quantity) {

        return orderService.placeOrder(
                productId,
                email,
                quantity);
    }

    @GetMapping
    public List<OrderResponse> getOrders(
            @RequestParam String email) {

        return orderService.getOrders(
                email);
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