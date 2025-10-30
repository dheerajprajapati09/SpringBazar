package in.dk.SpringBazaar.Service;

import in.dk.SpringBazaar.Dto.ProductDTO;
import in.dk.SpringBazaar.Entity.Product;
import in.dk.SpringBazaar.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setQuantity(productDTO.getQuantity());
        product.setPricePerUnit(productDTO.getPricePerUnit());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product updateProduct(Long id, ProductDTO productDTO) {
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isEmpty()) {
            throw new RuntimeException("Product with ID " + id + " not found.");
        }

        Product existingProduct = existingProductOpt.get();

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setPricePerUnit(productDTO.getPricePerUnit());

        return productRepository.save(existingProduct);
    }

    public void deleteProductByName(String name) {
        productRepository.deleteByNameContainingIgnoreCase(name);
    }
}
