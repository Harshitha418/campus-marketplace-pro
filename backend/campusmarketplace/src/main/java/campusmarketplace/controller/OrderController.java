package campusmarketplace.controller;

import campusmarketplace.dto.OrderSummaryResponse;
import campusmarketplace.dto.OrderDetailResponse;
import campusmarketplace.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.service.BillService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final BillService billService;

    public OrderController(OrderService orderService, BillService billService) {
        this.orderService = orderService;
        this.billService = billService;
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

    /** Download a PDF bill for one order. */
    @GetMapping("/{orderId}/bill")
    public ResponseEntity<byte[]> downloadBill(@PathVariable Long orderId) {

        byte[] pdf = billService.generateBill(orderId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=bill-order-" + orderId + ".pdf")
                .body(pdf);
    }
}