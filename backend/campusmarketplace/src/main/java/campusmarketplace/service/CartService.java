package campusmarketplace.service;

import campusmarketplace.entity.Cart;
import campusmarketplace.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(
            CartRepository cartRepository) {

        this.cartRepository = cartRepository;
    }

    public String addToCart(
            Long productId,
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

    public List<Cart> getCart(
            String userEmail) {

        return cartRepository
                .findByUserEmail(userEmail);
    }

    public String removeFromCart(
            Long id) {

        cartRepository.deleteById(id);

        return "Removed from cart";
    }
}