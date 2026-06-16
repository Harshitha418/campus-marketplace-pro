package campusmarketplace.service;

import java.util.List;
import campusmarketplace.dto.CreateProductRequest;
import campusmarketplace.entity.Product;
import campusmarketplace.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(
            ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    public String createProduct(
            CreateProductRequest request) {

        Product product = new Product();

        product.setTitle(request.getTitle());
        product.setDescription(
                request.getDescription());
        product.setPrice(request.getPrice());
        product.setSellerEmail(
                request.getSellerEmail());

        productRepository.save(product);

        return "Product created successfully";
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}