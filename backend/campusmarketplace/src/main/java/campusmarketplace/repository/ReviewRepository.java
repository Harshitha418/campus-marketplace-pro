package campusmarketplace.repository;

import campusmarketplace.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReviewRepository
                extends JpaRepository<Review, Long> {

        List<Review> findByProductId(
                        Long productId);

        long count();

        @Query("""
                        SELECT COALESCE(AVG(r.rating),0)
                        FROM Review r
                        """)
        Double getAverageRating();
}