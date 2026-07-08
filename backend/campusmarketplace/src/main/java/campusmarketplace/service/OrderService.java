package campusmarketplace.service;

import campusmarketplace.entity.OrderEntity;
import campusmarketplace.repository.OrderRepository;
import org.springframework.stereotype.Service;
import campusmarketplace.dto.OrderResponse;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
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

    public List<OrderResponse> getOrders(String userEmail) {

        List<OrderEntity> orders = orderRepository.findByUserEmail(userEmail);

        List<OrderResponse> response = new ArrayList<>();

        for (OrderEntity order : orders) {

            Product product = productRepository.findById(order.getProductId())
                    .orElse(null);

            if (product != null) {

                OrderResponse dto = new OrderResponse();

                dto.setId(order.getId());
                dto.setProductId(product.getId());
                dto.setTitle(product.getTitle());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setCategory(product.getCategory());
                dto.setQuantity(order.getQuantity());
                dto.setStatus(order.getStatus());
                dto.setImageUrl(product.getImageUrl());
                response.add(dto);
            }
        }

        return response;
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