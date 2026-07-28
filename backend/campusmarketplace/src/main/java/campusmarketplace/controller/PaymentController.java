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

    /**
     * Step 1: frontend asks for a Razorpay order before opening checkout.
     * We take the amount from the request; in a stricter design you'd
     * recompute it from the user's cart server-side so the client can't
     * understate the total.
     */
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(
            @RequestParam Double amount) {

        return paymentService.createOrder(amount);
    }

    /**
     * Step 4: verify the signature, and only if valid, place the order
     * (convert the cart into orders). If verification fails, nothing is placed.
     */
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
        // Signature is genuine -> place the order, storing the payment id
        // as the transaction reference for the bill.
        return orderService.checkout(
                authentication.getName(),
                request.getRazorpayPaymentId());
    }
}