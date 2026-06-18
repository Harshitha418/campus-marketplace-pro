package campusmarketplace.service;

import java.util.List;
import campusmarketplace.dto.CreateProductRequest;
import campusmarketplace.dto.UpdateProductRequest;
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

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElse(null);
    }

    public String updateProduct(
            Long id,
            UpdateProductRequest request) {

        Product product = productRepository.findById(id)
                .orElse(null);

        if (product == null) {
            return "Product not found";
        }

        product.setTitle(request.getTitle());
        product.setDescription(
                request.getDescription());
        product.setPrice(request.getPrice());

        productRepository.save(product);

        return "Product updated successfully";
    }

    public String deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElse(null);

        if (product == null) {
            return "Product not found";
        }

        productRepository.delete(product);

        return "Product deleted successfully";
    }

    public List<Product> searchProducts(
            String title) {

        return productRepository
                .findByTitleContainingIgnoreCase(
                        title);
    }

    public List<Product> getSellerProducts(
            String email) {

        return productRepository
                .findBySellerEmail(email);
    }

    public List<Product> getProductsLowToHigh() {

        return productRepository
                .findAllByOrderByPriceAsc();
    }

    public List<Product> getProductsHighToLow() {

        return productRepository
                .findAllByOrderByPriceDesc();
    }

}