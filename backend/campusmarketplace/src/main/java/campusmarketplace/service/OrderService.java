package campusmarketplace.service;

import campusmarketplace.entity.OrderEntity;
import campusmarketplace.repository.OrderRepository;
import org.springframework.stereotype.Service;
import campusmarketplace.dto.OrderResponse;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import campusmarketplace.entity.Cart;
import campusmarketplace.repository.CartRepository;
import campusmarketplace.exception.BadRequestException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            CartRepository cartRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    public String placeOrder(
            UUID productId,
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
                dto.setUserEmail(order.getUserEmail());
                dto.setImageUrl(product.getImageUrl());
                response.add(dto);
            }
        }

        return response;
    }

    public List<OrderResponse> getAllOrders() {

        List<OrderEntity> orders = orderRepository.findAll();

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
                dto.setUserEmail(order.getUserEmail());
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

    /**
     * Converts the user's entire cart into orders, then empties the cart.
     * 
     * @Transactional means: if any step fails, the whole thing rolls back —
     *                we never want orders created but the cart left full, or vice
     *                versa.
     */
    @Transactional
    public String checkout(String userEmail) {

        List<Cart> cartItems = cartRepository.findByUserEmail(userEmail);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new BadRequestException("Product no longer exists"));

            Integer stock = product.getStock() == null ? 0 : product.getStock();

            if (stock < cart.getQuantity()) {
                throw new BadRequestException(
                        "Not enough stock for '" + product.getTitle()
                                + "'. Only " + stock + " left.");
            }

            // Decrement stock.
            product.setStock(stock - cart.getQuantity());
            productRepository.save(product);

            OrderEntity order = new OrderEntity();

            order.setProductId(cart.getProductId());
            order.setUserEmail(userEmail);
            order.setQuantity(cart.getQuantity());
            order.setStatus("PLACED");

            orderRepository.save(order);
        }

        // Cart has been converted to orders — clear it.
        cartRepository.deleteAll(cartItems);

        return "Order placed successfully! " + cartItems.size() + " item(s) ordered.";
    }
}