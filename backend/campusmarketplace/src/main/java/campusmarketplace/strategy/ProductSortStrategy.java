package campusmarketplace.strategy;

import campusmarketplace.entity.Product;
import java.util.List;

/**
 * Strategy interface: every sorting algorithm implements this.
 * New sort orders can be added as new classes without changing existing code.
 */
public interface ProductSortStrategy {

    List<Product> sort(List<Product> products);

    /** The key clients use to select this strategy, e.g. "price_asc". */
    String getKey();
}