package in.dk.SpringBazaar.Controller;

import in.dk.SpringBazaar.Dto.ProductDTO;
import in.dk.SpringBazaar.Entity.Product;
import in.dk.SpringBazaar.Service.ProductService;
import in.dk.SpringBazaar.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestParam String email, @RequestBody ProductDTO productDTO) {
        if (!userService.isAdmin(email)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("error", email + " is not admin"));
        }
        return ResponseEntity.ok(productService.addProduct(productDTO));
    }

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@RequestParam String email,
                                           @PathVariable Long id,
                                           @RequestBody ProductDTO productDTO) {
        if (!userService.isAdmin(email)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("error", email + " is not admin"));
        }
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(404)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam String email, @RequestParam String name) {
        if (!userService.isAdmin(email)) {
            return ResponseEntity
                    .status(403)
                    .body(Map.of("error", email + " is not admin"));
        }

        productService.deleteProductByName(name);
        return ResponseEntity.ok(Map.of("message", "Products containing '" + name + "' deleted successfully"));
    }
}
