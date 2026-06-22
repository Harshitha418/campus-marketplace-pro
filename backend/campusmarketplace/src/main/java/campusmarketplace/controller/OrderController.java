package campusmarketplace.controller;

import campusmarketplace.entity.OrderEntity;
import campusmarketplace.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestParam Long productId,
            @RequestParam String email,
            @RequestParam Integer quantity) {

        return orderService.placeOrder(
                productId,
                email,
                quantity);
    }

    @GetMapping
    public List<OrderEntity> getOrders(
            @RequestParam String email) {

        return orderService.getOrders(
                email);
    }

    @PutMapping("/{id}")
    public String updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return orderService
                .updateStatus(id, status);
    }
}