package campusmarketplace.repository;

import campusmarketplace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository
                extends JpaRepository<Product, Long> {

        List<Product> findByTitleContainingIgnoreCase(
                        String title);

        List<Product> findBySellerEmail(
                        String sellerEmail);

        List<Product> findAllByOrderByPriceAsc();

        List<Product> findAllByOrderByPriceDesc();
}