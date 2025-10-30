package in.dk.SpringBazaar.Repository;

import in.dk.SpringBazaar.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    void deleteByNameContainingIgnoreCase(String name);
}
