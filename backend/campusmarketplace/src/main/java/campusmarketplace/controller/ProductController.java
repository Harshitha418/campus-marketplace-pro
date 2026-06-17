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
}