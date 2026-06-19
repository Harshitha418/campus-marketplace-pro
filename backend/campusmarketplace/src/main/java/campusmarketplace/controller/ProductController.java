package campusmarketplace.controller;

import campusmarketplace.dto.CreateProductRequest;
import campusmarketplace.dto.UpdateProductRequest;
import campusmarketplace.service.ProductService;
import org.springframework.web.bind.annotation.*;
import campusmarketplace.entity.Product;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(
            ProductService productService) {

        this.productService = productService;
    }

    @PostMapping
    public String createProduct(
            @RequestBody CreateProductRequest request) {

        return productService
                .createProduct(request);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable Long id) {

        return productService.getProduct(id);
    }

    @PutMapping("/{id}")
    public String updateProduct(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request) {

        return productService
                .updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(
            @PathVariable Long id) {

        return productService
                .deleteProduct(id);
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
}