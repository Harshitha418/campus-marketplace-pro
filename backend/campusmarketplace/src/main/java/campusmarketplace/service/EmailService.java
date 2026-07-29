package campusmarketplace.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a plain-text order confirmation.
     * Wrapped so a mail failure never breaks checkout — the order is
     * already placed; email is a nice-to-have, not part of the transaction.
     */
    public void sendOrderConfirmation(String toEmail, Long orderId, Double total, int itemCount) {

        try {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(toEmail);
            message.setSubject("Order Confirmed — Campus Marketplace (Order #" + orderId + ")");
            message.setText(
                    "Hi,\n\n" +
                            "Thank you for your order on Campus Marketplace!\n\n" +
                            "Order ID: #" + orderId + "\n" +
                            "Items: " + itemCount + "\n" +
                            "Total Paid: Rs. " + String.format("%.2f", total) + "\n\n" +
                            "You can view your order details and download the bill from your Orders page.\n\n" +
                            "Happy shopping,\n" +
                            "Campus Marketplace Team");

            mailSender.send(message);

        } catch (Exception e) {
            // Log and move on — never fail the order because of an email issue.
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }

    }
}