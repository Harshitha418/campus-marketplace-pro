package campusmarketplace.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import campusmarketplace.dto.CreateProductRequest;
import campusmarketplace.dto.RecommendationResponse;
import campusmarketplace.dto.UpdateProductRequest;
import campusmarketplace.entity.OrderEntity;
import campusmarketplace.entity.OrderItem;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.OrderRepository;
import campusmarketplace.repository.ProductRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import campusmarketplace.strategy.SortStrategyFactory;
import campusmarketplace.strategy.ProductSortStrategy;
import java.util.UUID;
import campusmarketplace.repository.OrderItemRepository;

@Service
public class ProductService {

        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;
        private final OrderItemRepository orderItemRepository;
        private final SortStrategyFactory sortStrategyFactory;

        public ProductService(
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        SortStrategyFactory sortStrategyFactory) {

                this.productRepository = productRepository;
                this.orderRepository = orderRepository;
                this.orderItemRepository = orderItemRepository;
                this.sortStrategyFactory = sortStrategyFactory;
        }

        @CacheEvict(value = "products", allEntries = true)
        public String createProduct(CreateProductRequest request, String requesterEmail) {

                Product product = new Product();

                product.setTitle(request.getTitle());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                // Trust the JWT-authenticated email, not whatever the client sent in the body.
                product.setSellerEmail(requesterEmail);
                product.setCategory(request.getCategory());
                product.setImageUrl(request.getImageUrl());
                product.setStock(request.getStock() == null ? 0 : request.getStock());

                productRepository.save(product);

                return "Product created successfully";
        }

        @Cacheable("products")
        public List<Product> getAllProducts() {
                return productRepository.findAll();
        }

        public List<Product> getSortedProducts(String sortBy) {

                List<Product> products = productRepository.findAll();

                ProductSortStrategy strategy = sortStrategyFactory.getStrategy(sortBy);

                if (strategy == null) {
                        // Unknown sort key — return unsorted rather than failing.
                        return products;
                }

                return strategy.sort(products);
        }

        public Page<Product> getAllProductsPaged(int page, int size) {
                return productRepository.findAll(PageRequest.of(page, size));
        }

        public Product getProduct(UUID id) {
                return productRepository.findById(id).orElse(null);
        }

        @CacheEvict(value = "products", allEntries = true)
        public String updateProduct(
                        UUID id,
                        UpdateProductRequest request,
                        String requesterEmail) {

                Product product = productRepository.findById(id).orElse(null);

                if (product == null) {
                        return "Product not found";
                }

                if (!product.getSellerEmail().equalsIgnoreCase(requesterEmail)) {
                        // Not the owner — do not allow the edit.
                        throw new AccessDeniedException(
                                        "You do not have permission to edit this product");
                }

                product.setTitle(request.getTitle());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                if (request.getStock() != null) {
                        product.setStock(request.getStock());
                }

                productRepository.save(product);

                return "Product updated successfully";
        }

        @CacheEvict(value = "products", allEntries = true)
        public String deleteProduct(UUID id, String requesterEmail) {

                Product product = productRepository.findById(id).orElse(null);

                if (product == null) {
                        return "Product not found";
                }

                if (!product.getSellerEmail().equalsIgnoreCase(requesterEmail)) {
                        throw new AccessDeniedException(
                                        "You do not have permission to delete this product");
                }

                productRepository.delete(product);

                return "Product deleted successfully";
        }

        public List<Product> searchProducts(String title) {

                return productRepository
                                .findByTitleContainingIgnoreCase(title);
        }

        public List<Product> getSellerProducts(String email) {

                return productRepository.findBySellerEmail(email);
        }

        public List<Product> getProductsLowToHigh() {

                return productRepository.findAllByOrderByPriceAsc();
        }

        public List<Product> getProductsHighToLow() {

                return productRepository.findAllByOrderByPriceDesc();
        }

        public List<Product> getProductsByCategory(String category) {

                return productRepository.findByCategoryIgnoreCase(category);
        }

        public List<RecommendationResponse> getRecommendations(String email) {

                List<OrderEntity> orders = orderRepository.findByUserEmailOrderByCreatedAtDesc(email);

                Set<String> categories = new HashSet<>();

                for (OrderEntity order : orders) {

                        for (OrderItem item : orderItemRepository.findByOrderId(order.getId())) {

                                Product product = productRepository
                                                .findById(item.getProductId())
                                                .orElse(null);

                                if (product != null) {
                                        categories.add(product.getCategory());
                                }
                        }
                }

                List<RecommendationResponse> recommendations = new ArrayList<>();

                for (String category : categories) {

                        List<Product> products = productRepository.findByCategory(category);

                        for (Product product : products) {

                                RecommendationResponse response = new RecommendationResponse();

                                response.setId(product.getId());
                                response.setTitle(product.getTitle());
                                response.setCategory(product.getCategory());
                                response.setPrice(product.getPrice());

                                recommendations.add(response);
                        }
                }

                return recommendations;
        }

}