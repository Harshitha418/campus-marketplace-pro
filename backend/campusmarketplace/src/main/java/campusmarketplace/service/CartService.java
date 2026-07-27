package campusmarketplace.service;

import campusmarketplace.entity.Cart;
import campusmarketplace.repository.CartRepository;
import org.springframework.stereotype.Service;
import campusmarketplace.dto.CartResponse;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import campusmarketplace.exception.BadRequestException;
import campusmarketplace.exception.ResourceNotFoundException;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository) {

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public String addToCart(
            UUID productId,
            String userEmail) {

        Cart cart = cartRepository
                .findByProductIdAndUserEmail(
                        productId,
                        userEmail)
                .orElse(null);

        if (cart != null) {

            cart.setQuantity(
                    cart.getQuantity() + 1);

        } else {

            cart = new Cart();

            cart.setProductId(productId);
            cart.setUserEmail(userEmail);
            cart.setQuantity(1);
        }

        cartRepository.save(cart);

        return "Added to cart";
    }

    public List<CartResponse> getCart(String userEmail) {

        List<Cart> cartItems = cartRepository.findByUserEmail(userEmail);

        List<CartResponse> response = new ArrayList<>();

        for (Cart cart : cartItems) {

            Product product = productRepository.findById(cart.getProductId())
                    .orElse(null);

            if (product != null) {

                CartResponse item = new CartResponse();

                item.setId(cart.getId());
                item.setProductId(product.getId());
                item.setTitle(product.getTitle());
                item.setDescription(product.getDescription());
                item.setPrice(product.getPrice());
                item.setCategory(product.getCategory());
                item.setQuantity(cart.getQuantity());
                item.setImageUrl(product.getImageUrl());
                response.add(item);
            }
        }

        return response;
    }

    public String removeFromCart(Long id, String userEmail) {

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cart.getUserEmail().equalsIgnoreCase(userEmail)) {
            throw new BadRequestException("This item does not belong to your cart");
        }

        cartRepository.delete(cart);

        return "Removed from cart";
    }

    /**
     * Sets a cart line to an exact quantity. If it drops to 0 or below,
     * the line is removed. Ownership is verified so a user can only
     * modify their own cart lines.
     */
    public String updateQuantity(Long cartId, Integer quantity, String userEmail) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        // Ownership check: the line must belong to the caller.
        if (!cart.getUserEmail().equalsIgnoreCase(userEmail)) {
            throw new BadRequestException("This item does not belong to your cart");
        }

        if (quantity <= 0) {
            cartRepository.delete(cart);
            return "Removed from cart";
        }

        cart.setQuantity(quantity);
        cartRepository.save(cart);

        return "Quantity updated";
    }
}