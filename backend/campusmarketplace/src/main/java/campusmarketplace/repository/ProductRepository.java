package campusmarketplace.repository;

import campusmarketplace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProductRepository
                extends JpaRepository<Product, UUID> {

        long count();

        Page<Product> findByTitleContainingIgnoreCase(
                        String title, Pageable pageable);

        List<Product> findBySellerEmail(
                        String sellerEmail);

        List<Product> findAllByOrderByPriceAsc();

        List<Product> findAllByOrderByPriceDesc();

        Page<Product> findByCategoryIgnoreCase(
                        String category, Pageable pageable);

        List<Product> findByCategory(String category);
}