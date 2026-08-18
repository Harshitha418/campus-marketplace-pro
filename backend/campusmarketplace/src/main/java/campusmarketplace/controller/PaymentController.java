package campusmarketplace.controller;

import campusmarketplace.dto.PaymentVerificationRequest;
import campusmarketplace.service.OrderService;
import campusmarketplace.service.PaymentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(
            PaymentService paymentService,
            OrderService orderService) {

        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @PostMapping("/create-order")
    public Map<String, Object> createOrder(
            Authentication authentication) {

        return paymentService.createOrder(authentication.getName());
    }

    @PostMapping("/verify")
    public String verifyAndPlaceOrder(
            @RequestBody PaymentVerificationRequest request,
            Authentication authentication) {

        boolean valid = paymentService.verifyPayment(
                request.getRazorpayOrderId(),
                request.getRazorpayPaymentId(),
                request.getRazorpaySignature());

        if (!valid) {
            return "Payment verification failed";
        }

        return orderService.checkout(authentication.getName(), request.getRazorpayPaymentId());
    }
}