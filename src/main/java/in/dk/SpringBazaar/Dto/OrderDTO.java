package in.dk.SpringBazaar.Dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDTO {
    private String email;
    private List<Long> productIds;
    private List<Integer> quantities;
}
