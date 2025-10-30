package in.dk.SpringBazaar.Service;

import in.dk.SpringBazaar.Dto.ProductDTO;
import in.dk.SpringBazaar.Entity.Product;
import in.dk.SpringBazaar.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void deleteProductByName(String name) {
        productRepository.deleteByNameContainingIgnoreCase(name);
    }
}
