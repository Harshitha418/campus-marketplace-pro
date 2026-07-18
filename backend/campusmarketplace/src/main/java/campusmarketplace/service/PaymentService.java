package campusmarketplace.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import campusmarketplace.exception.BadRequestException;
import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private RazorpayClient razorpayClient;

    @PostConstruct
    public void init() throws Exception {
        this.razorpayClient = new RazorpayClient(keyId, keySecret);
    }

    /**
     * Step 1 of the flow: create a Razorpay order for the given amount.
     * Amount is in the smallest currency unit — paise — so we multiply by 100.
     * Returns the details the frontend needs to open the checkout popup.
     */
    public Map<String, Object> createOrder(Double amount) {

        try {

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (amount * 100)); // rupees -> paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "rcpt_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("keyId", keyId); // frontend needs the public key

            return response;

        } catch (Exception e) {
            throw new BadRequestException("Could not create payment order");
        }
    }

    /**
     * Step 4 of the flow: verify the payment signature Razorpay sent back.
     * This proves the payment is genuine and wasn't faked by the client.
     * The signature is an HMAC of "orderId|paymentId" signed with our secret —
     * only Razorpay (who also knows the secret) could have produced it.
     */
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