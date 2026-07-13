package campusmarketplace.repository;

import campusmarketplace.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface ReviewRepository
                extends JpaRepository<Review, Long> {

        List<Review> findByProductId(
                        UUID productId);

        long count();

        @Query("""
                        SELECT COALESCE(AVG(r.rating),0)
                        FROM Review r
                        """)
        Double getAverageRating();
}