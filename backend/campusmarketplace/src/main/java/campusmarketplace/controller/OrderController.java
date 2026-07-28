package campusmarketplace.controller;

import campusmarketplace.dto.OrderSummaryResponse;
import campusmarketplace.dto.OrderDetailResponse;
import campusmarketplace.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /** The logged-in user's orders as summary rows (newest first). */
    @GetMapping
    public List<OrderSummaryResponse> getMyOrders(Authentication authentication) {
        return orderService.getMyOrders(authentication.getName());
    }

    /** Full detail of one order. */
    @GetMapping("/{orderId}")
    public OrderDetailResponse getOrderDetail(@PathVariable Long orderId) {
        return orderService.getOrderDetail(orderId);
    }

    /** Admin: all orders as summary rows. */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderSummaryResponse> getAllOrders() {
        return orderService.getAllOrderSummaries();
    }

    /** Admin: update the status of a single line item. */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/item/{itemId}")
    public String updateItemStatus(
            @PathVariable Long itemId,
            @RequestParam String status) {
        return orderService.updateItemStatus(itemId, status);
    }
}