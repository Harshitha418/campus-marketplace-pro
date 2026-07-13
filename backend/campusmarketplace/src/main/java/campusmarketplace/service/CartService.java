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

    public String removeFromCart(
            Long id) {

        cartRepository.deleteById(id);

        return "Removed from cart";
    }
}