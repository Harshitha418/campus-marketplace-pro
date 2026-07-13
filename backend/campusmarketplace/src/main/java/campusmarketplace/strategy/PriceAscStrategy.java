package campusmarketplace.strategy;

import campusmarketplace.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceAscStrategy implements ProductSortStrategy {

    @Override
    public List<Product> sort(List<Product> products) {
        return products.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public String getKey() {
        return "price_asc";
    }
}