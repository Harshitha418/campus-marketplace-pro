package campusmarketplace.service;

import campusmarketplace.entity.Wishlist;
import campusmarketplace.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import campusmarketplace.dto.WishlistResponse;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.ProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository) {

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    public String addToWishlist(
            UUID productId,
            String userEmail) {

        Wishlist wishlist = new Wishlist();

        wishlist.setProductId(productId);
        wishlist.setUserEmail(userEmail);

        wishlistRepository.save(wishlist);

        return "Added to wishlist";
    }

    public List<WishlistResponse> getWishlist(String userEmail) {

        List<Wishlist> wishlist = wishlistRepository.findByUserEmail(userEmail);

        List<WishlistResponse> response = new ArrayList<>();

        for (Wishlist item : wishlist) {

            Product product = productRepository.findById(item.getProductId())
                    .orElse(null);

            if (product != null) {

                WishlistResponse dto = new WishlistResponse();

                dto.setId(item.getId());
                dto.setProductId(product.getId());
                dto.setTitle(product.getTitle());
                dto.setDescription(product.getDescription());
                dto.setPrice(product.getPrice());
                dto.setCategory(product.getCategory());
                dto.setImageUrl(product.getImageUrl());
                response.add(dto);
            }
        }

        return response;
    }

    public String removeFromWishlist(
            Long id) {

        wishlistRepository.deleteById(id);

        return "Removed from wishlist";
    }
}