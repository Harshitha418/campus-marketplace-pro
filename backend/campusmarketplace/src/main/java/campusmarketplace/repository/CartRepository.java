package campusmarketplace.repository;

import campusmarketplace.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CartRepository
        extends JpaRepository<Cart, Long> {

    List<Cart> findByUserEmail(
            String userEmail);

    Optional<Cart> findByProductIdAndUserEmail(
            Long productId,
            String userEmail);
}