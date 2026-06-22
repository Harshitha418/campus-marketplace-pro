package campusmarketplace.service;

import campusmarketplace.entity.OrderEntity;
import campusmarketplace.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(
            OrderRepository orderRepository) {

        this.orderRepository = orderRepository;
    }

    public String placeOrder(
            Long productId,
            String userEmail,
            Integer quantity) {

        OrderEntity order = new OrderEntity();

        order.setProductId(productId);
        order.setUserEmail(userEmail);
        order.setQuantity(quantity);
        order.setStatus("PLACED");

        orderRepository.save(order);

        return "Order placed successfully";
    }

    public List<OrderEntity> getOrders(
            String userEmail) {

        return orderRepository.findByUserEmail(
                userEmail);
    }

    public String updateStatus(
            Long id,
            String status) {

        OrderEntity order = orderRepository.findById(id)
                .orElse(null);

        if (order == null) {
            return "Order not found";
        }

        order.setStatus(status);

        orderRepository.save(order);

        return "Status updated";
    }
}