package in.dk.SpringBazaar.Dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private List<ProductDTO> products;
    private List<Integer> quantities;
    private double totalAmount;
}
