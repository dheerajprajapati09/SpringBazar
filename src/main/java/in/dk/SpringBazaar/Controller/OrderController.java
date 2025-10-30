package in.dk.SpringBazaar.Controller;

import in.dk.SpringBazaar.Dto.OrderDTO;
import in.dk.SpringBazaar.Dto.OrderResponseDTO;
import in.dk.SpringBazaar.Entity.Order;
import in.dk.SpringBazaar.Service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public OrderResponseDTO placeOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.placeOrder(orderDTO);
    }

    @GetMapping("/user")
    public List<Order> getOrdersByUser(@RequestParam String email) {
        return orderService.getOrdersByUser(email);
    }
}
