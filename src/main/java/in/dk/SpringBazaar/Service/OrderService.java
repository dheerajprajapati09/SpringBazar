package in.dk.SpringBazaar.Service;

import in.dk.SpringBazaar.Dto.OrderDTO;
import in.dk.SpringBazaar.Dto.OrderResponseDTO;
import in.dk.SpringBazaar.Dto.ProductDTO;
import in.dk.SpringBazaar.Entity.Order;
import in.dk.SpringBazaar.Entity.Product;
import in.dk.SpringBazaar.Entity.User;
import in.dk.SpringBazaar.Repository.OrderRepository;
import in.dk.SpringBazaar.Repository.ProductRepository;
import in.dk.SpringBazaar.Repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public OrderResponseDTO placeOrder(OrderDTO orderDTO) {

        User user = userRepository.findByEmail(orderDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + orderDTO.getEmail()));

        List<Long> productIds = orderDTO.getProductIds();
        List<Integer> quantities = orderDTO.getQuantities();

        if (productIds.size() != quantities.size()) {
            throw new RuntimeException("Products and quantities list must have the same size");
        }

        List<Product> products = new ArrayList<>();
        for (int i = 0; i < productIds.size(); i++) {
            Long id = productIds.get(i);
            int orderedQuantity = quantities.get(i);

            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

            if (product.getQuantity() < orderedQuantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setQuantity(product.getQuantity() - orderedQuantity);
            productRepository.save(product);

            products.add(product);
        }

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setQuantities(quantities);

        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setId(savedOrder.getId());
        responseDTO.setUserId(savedOrder.getUser().getId());
        responseDTO.setTotalAmount(savedOrder.getTotalAmount());
        responseDTO.setQuantities(savedOrder.getQuantities());

        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product p : savedOrder.getProducts()) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(p.getId());
            productDTO.setName(p.getName());
            productDTO.setDescription(p.getDescription());
            productDTO.setQuantity(p.getQuantity());
            productDTO.setPricePerUnit(p.getPricePerUnit());
            productDTOs.add(productDTO);
        }
        responseDTO.setProducts(productDTOs);

        return responseDTO;
    }


    public List<Order> getOrdersByUser(String email) {
        return orderRepository.findByUserEmail(email);
    }
}
