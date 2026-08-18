package campusmarketplace.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import campusmarketplace.entity.Cart;
import campusmarketplace.entity.Product;
import campusmarketplace.exception.BadRequestException;
import campusmarketplace.repository.CartRepository;
import campusmarketplace.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public PaymentService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    public Map<String, Object> createOrder(String userEmail) {

        List<Cart> cartItems = cartRepository.findByUserEmail(userEmail);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        double total = 0.0;
        for (Cart item : cartItems) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new BadRequestException("Product no longer exists"));
            total += product.getPrice() * item.getQuantity();
        }

        try {

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", Math.round(total * 100));
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("keyId", keyId);

            return response;

        } catch (Exception e) {
            throw new BadRequestException("Could not create payment order");
        }
    }

    public boolean verifyPayment(
            String orderId,
            String paymentId,
            String signature) {

        try {

            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            return Utils.verifyPaymentSignature(options, keySecret);

        } catch (Exception e) {
            return false;
        }
    }
}