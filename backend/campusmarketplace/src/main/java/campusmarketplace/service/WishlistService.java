package campusmarketplace.service;

import campusmarketplace.entity.Wishlist;
import campusmarketplace.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    public WishlistService(
            WishlistRepository wishlistRepository) {

        this.wishlistRepository = wishlistRepository;
    }

    public String addToWishlist(
            Long productId,
            String userEmail) {

        Wishlist wishlist = new Wishlist();

        wishlist.setProductId(productId);
        wishlist.setUserEmail(userEmail);

        wishlistRepository.save(wishlist);

        return "Added to wishlist";
    }

    public List<Wishlist> getWishlist(
            String userEmail) {

        return wishlistRepository
                .findByUserEmail(userEmail);
    }

    public String removeFromWishlist(
            Long id) {

        wishlistRepository.deleteById(id);

        return "Removed from wishlist";
    }
}