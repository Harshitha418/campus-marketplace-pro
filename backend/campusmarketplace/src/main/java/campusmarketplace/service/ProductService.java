package campusmarketplace.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import campusmarketplace.dto.CreateProductRequest;
import campusmarketplace.dto.RecommendationResponse;
import campusmarketplace.dto.UpdateProductRequest;
import campusmarketplace.entity.OrderEntity;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.OrderRepository;
import campusmarketplace.repository.ProductRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

        private final ProductRepository productRepository;
        private final OrderRepository orderRepository;

        public ProductService(
                        ProductRepository productRepository,
                        OrderRepository orderRepository) {

                this.productRepository = productRepository;
                this.orderRepository = orderRepository;
        }

        @CacheEvict(value = "products", allEntries = true)
        public String createProduct(CreateProductRequest request) {

                Product product = new Product();

                product.setTitle(request.getTitle());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());
                product.setSellerEmail(request.getSellerEmail());
                product.setCategory(request.getCategory());

                productRepository.save(product);

                return "Product created successfully";
        }

        @Cacheable("products")
        public List<Product> getAllProducts() {
                return productRepository.findAll();
        }

        public Product getProduct(Long id) {
                return productRepository.findById(id).orElse(null);
        }

        @CacheEvict(value = "products", allEntries = true)
        public String updateProduct(
                        Long id,
                        UpdateProductRequest request) {

                Product product = productRepository.findById(id).orElse(null);

                if (product == null) {
                        return "Product not found";
                }

                product.setTitle(request.getTitle());
                product.setDescription(request.getDescription());
                product.setPrice(request.getPrice());

                productRepository.save(product);

                return "Product updated successfully";
        }

        @CacheEvict(value = "products", allEntries = true)
        public String deleteProduct(Long id) {

                Product product = productRepository.findById(id).orElse(null);

                if (product == null) {
                        return "Product not found";
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

                List<OrderEntity> orders = orderRepository.findByUserEmail(email);

                Set<String> categories = new HashSet<>();

                for (OrderEntity order : orders) {

                        Product product = productRepository
                                        .findById(order.getProductId())
                                        .orElse(null);

                        if (product != null) {
                                categories.add(product.getCategory());
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