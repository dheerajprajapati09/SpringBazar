package in.dk.SpringBazaar.Dto;
import lombok.Data;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private int quantity;
    private double pricePerUnit;
}
