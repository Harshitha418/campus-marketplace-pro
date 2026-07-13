package campusmarketplace.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import campusmarketplace.dto.CreateProductRequest;
import campusmarketplace.dto.RecommendationResponse;
import campusmarketplace.dto.UpdateProductRequest;
import campusmarketplace.service.ProductService;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import java.util.List;
import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

        private final ProductService productService;
        private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

        public ProductController(
                        ProductService productService) {

                this.productService = productService;
        }

        @PostMapping
        public String createProduct(
                        @Valid @RequestBody CreateProductRequest request,
                        Authentication authentication) {

                // authentication.getName() is the email JwtAuthenticationFilter put
                // into the SecurityContext after validating the JWT.
                return productService
                                .createProduct(request, authentication.getName());
        }

        @GetMapping
        public List<Product> getAllProducts() {
                return productService.getAllProducts();
        }

        @GetMapping("/sorted")
        public List<Product> getSortedProducts(
                        @RequestParam(defaultValue = "price_asc") String sortBy) {
                return productService.getSortedProducts(sortBy);
        }

        @GetMapping("/paged")
        public Page<Product> getAllProductsPaged(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {

                return productService.getAllProductsPaged(page, size);
        }

        @GetMapping("/{id}")
        public Product getProduct(
                        @PathVariable UUID id) {

                return productService.getProduct(id);
        }

        @PutMapping("/{id}")
        public String updateProduct(
                        @PathVariable UUID id,
                        @RequestBody UpdateProductRequest request,
                        Authentication authentication) {

                return productService
                                .updateProduct(id, request, authentication.getName());
        }

        @DeleteMapping("/{id}")
        public String deleteProduct(
                        @PathVariable UUID id,
                        Authentication authentication) {

                return productService
                                .deleteProduct(id, authentication.getName());
        }

        @GetMapping("/search")
        public List<Product> searchProducts(
                        @RequestParam String title) {

                return productService
                                .searchProducts(title);
        }

        @GetMapping("/seller")
        public List<Product> sellerProducts(
                        @RequestParam String email) {

                return productService
                                .getSellerProducts(email);
        }

        @GetMapping("/price/low")
        public List<Product> lowToHigh() {
                return productService
                                .getProductsLowToHigh();
        }

        @GetMapping("/price/high")
        public List<Product> highToLow() {
                return productService
                                .getProductsHighToLow();
        }

        @GetMapping("/category/{category}")
        public List<Product> getProductsByCategory(
                        @PathVariable String category) {

                return productService
                                .getProductsByCategory(
                                                category);
        }

        @GetMapping("/recommended/{email}")
        public List<RecommendationResponse> recommendedProducts(
                        @PathVariable String email) {
                logger.info("Recommendation API hit for user: {}", email);
                return productService.getRecommendations(email);
        }
}