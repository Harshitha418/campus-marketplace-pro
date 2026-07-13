package campusmarketplace.repository;

import campusmarketplace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ProductRepository
                extends JpaRepository<Product, UUID> {

        long count();

        List<Product> findByTitleContainingIgnoreCase(
                        String title);

        List<Product> findBySellerEmail(
                        String sellerEmail);

        List<Product> findAllByOrderByPriceAsc();

        List<Product> findAllByOrderByPriceDesc();

        List<Product> findByCategoryIgnoreCase(
                        String category);

        List<Product> findByCategory(String category);
}