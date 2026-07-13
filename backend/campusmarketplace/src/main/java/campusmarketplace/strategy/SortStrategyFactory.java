package campusmarketplace.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Looks up the right ProductSortStrategy by its key.
 * Spring injects every ProductSortStrategy bean into the constructor list,
 * so adding a new strategy class automatically registers it here — no edits
 * needed.
 */
@Component
public class SortStrategyFactory {

    private final Map<String, ProductSortStrategy> strategies;

    public SortStrategyFactory(List<ProductSortStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ProductSortStrategy::getKey, Function.identity()));
    }

    /** Returns the strategy for the given key, or null if none matches. */
    public ProductSortStrategy getStrategy(String key) {
        return strategies.get(key);
    }
}