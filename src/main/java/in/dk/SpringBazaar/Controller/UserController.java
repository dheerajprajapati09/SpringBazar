package in.dk.SpringBazaar.Controller;

import in.dk.SpringBazaar.Dto.AuthResponse;
import in.dk.SpringBazaar.Dto.LoginRequest;
import in.dk.SpringBazaar.Dto.RegisterDto;
import in.dk.SpringBazaar.Entity.User;
import in.dk.SpringBazaar.Repository.UserRepository;
import in.dk.SpringBazaar.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterDto request) {
        String message = userService.register(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
        );
        return ResponseEntity.ok(message);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String refreshTokenHeader) {
        String refreshToken = refreshTokenHeader.replace("Bearer ", "");
        AuthResponse response = userService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
//        String email = authentication.getName();
//        RegisterDto user = userService.getByEmail(email);
//        return ResponseEntity.ok(user);
//    }
}
