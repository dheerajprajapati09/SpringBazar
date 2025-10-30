package in.dk.SpringBazaar.Dto;

import in.dk.SpringBazaar.Entity.Role;
import lombok.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterDto {

    private String name;

    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com)$",
            message = "Invalid email pattern"
    )
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&*]).{8,}$",
            message = "Password must contain at least one special character, one capital letter, and one number"
    )
    private String password;

    @NotNull(message = "Role is required")
    private Role role;

    public RegisterDto(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
