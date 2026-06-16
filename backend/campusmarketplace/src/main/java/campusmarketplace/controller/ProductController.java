package campusmarketplace.controller;

import campusmarketplace.dto.CreateProductRequest;
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

}