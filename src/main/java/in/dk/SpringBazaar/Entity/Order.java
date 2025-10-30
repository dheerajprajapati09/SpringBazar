package in.dk.SpringBazaar.Entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "order_quantities", joinColumns = @JoinColumn(name = "order_id"))
    @Column(name = "quantity")
    private List<Integer> quantities = new ArrayList<>();

    private double totalAmount;

    @PrePersist
    @PreUpdate
    public void calculateTotalAmount() {
        totalAmount = 0;
        for (int i = 0; i < products.size(); i++) {
            totalAmount += products.get(i).getPricePerUnit() * quantities.get(i);
        }
    }
}
