package campusmarketplace.service;

import campusmarketplace.entity.OrderEntity;
import campusmarketplace.entity.OrderItem;
import campusmarketplace.entity.Product;
import campusmarketplace.dto.OrderDetailResponse;
import campusmarketplace.dto.OrderSummaryResponse;
import campusmarketplace.entity.Cart;
import campusmarketplace.repository.OrderRepository;
import campusmarketplace.repository.OrderItemRepository;
import campusmarketplace.repository.ProductRepository;
import campusmarketplace.repository.CartRepository;
import campusmarketplace.exception.BadRequestException;
import campusmarketplace.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import campusmarketplace.dto.OrderSummaryResponse;
import campusmarketplace.dto.OrderDetailResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            ProductRepository productRepository,
            CartRepository cartRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    /**
     * Converts the user's whole cart into ONE order with many items.
     * Transactional: if any item fails the stock check, everything rolls back.
     */
    @Transactional
    public String checkout(String userEmail, String transactionId) {

        List<Cart> cartItems = cartRepository.findByUserEmail(userEmail);

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Your cart is empty");
        }

        // 1. Create the order (receipt) first so we have its id.
        OrderEntity order = new OrderEntity();
        order.setUserEmail(userEmail);
        order.setCreatedAt(LocalDateTime.now());
        order.setTransactionId(transactionId);
        order.setTotalAmount(0.0); // filled in as we add items
        order = orderRepository.save(order);

        double total = 0.0;

        // 2. Add each cart line as an order item, checking + decrementing stock.
        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElseThrow(() -> new BadRequestException("Product no longer exists"));

            int stock = product.getStock() == null ? 0 : product.getStock();

            if (stock < cart.getQuantity()) {
                throw new BadRequestException(
                        "Not enough stock for '" + product.getTitle()
                                + "'. Only " + stock + " left.");
            }

            product.setStock(stock - cart.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(product.getId());
            item.setQuantity(cart.getQuantity());
            item.setPriceAtPurchase(product.getPrice());
            item.setStatus("PLACED");
            orderItemRepository.save(item);

            total += product.getPrice() * cart.getQuantity();
        }

        // 3. Save the computed total on the order.
        order.setTotalAmount(total);
        orderRepository.save(order);

        // 4. Empty the cart.
        cartRepository.deleteAll(cartItems);

        return "Order placed successfully! " + cartItems.size() + " item(s) ordered.";
    }

    /** A user's orders as lightweight summary rows, newest first. */
    public List<OrderSummaryResponse> getMyOrders(String userEmail) {

        List<OrderEntity> orders = orderRepository.findByUserEmailOrderByCreatedAtDesc(userEmail);

        List<OrderSummaryResponse> result = new ArrayList<>();

        for (OrderEntity order : orders) {
            result.add(toSummary(order));
        }

        return result;
    }

    /** All orders as summary rows, newest first (admin). */
    public List<OrderSummaryResponse> getAllOrderSummaries() {

        List<OrderEntity> orders = orderRepository.findAllByOrderByCreatedAtDesc();

        List<OrderSummaryResponse> result = new ArrayList<>();

        for (OrderEntity order : orders) {
            result.add(toSummary(order));
        }

        return result;
    }

    /** Full detail of one order, including its line items. */
    public OrderDetailResponse getOrderDetail(Long orderId) {

        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderDetailResponse dto = new OrderDetailResponse();
        dto.setOrderId(order.getId());
        dto.setUserEmail(order.getUserEmail());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setTransactionId(order.getTransactionId());

        List<OrderDetailResponse.Item> items = new ArrayList<>();

        for (OrderItem oi : orderItemRepository.findByOrderId(order.getId())) {

            Product product = productRepository.findById(oi.getProductId()).orElse(null);

            OrderDetailResponse.Item item = new OrderDetailResponse.Item();
            item.setItemId(oi.getId());
            item.setProductId(oi.getProductId());
            item.setQuantity(oi.getQuantity());
            item.setPriceAtPurchase(oi.getPriceAtPurchase());
            item.setStatus(oi.getStatus());
            item.setTitle(product != null ? product.getTitle() : "Product removed");
            item.setImageUrl(product != null ? product.getImageUrl() : null);

            items.add(item);
        }

        dto.setItems(items);
        return dto;
    }

    /** Update the status of a single line item (admin). */
    public String updateItemStatus(Long itemId, String status) {

        OrderItem item = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found"));

        item.setStatus(status);
        orderItemRepository.save(item);

        return "Status updated";
    }

    /** Shared helper: build a summary row from an order. */
    private OrderSummaryResponse toSummary(OrderEntity order) {

        OrderSummaryResponse s = new OrderSummaryResponse();
        s.setOrderId(order.getId());
        s.setUserEmail(order.getUserEmail());
        s.setCreatedAt(order.getCreatedAt());
        s.setTotalAmount(order.getTotalAmount());
        s.setTransactionId(order.getTransactionId());
        s.setItemCount(orderItemRepository.findByOrderId(order.getId()).size());
        return s;
    }
}