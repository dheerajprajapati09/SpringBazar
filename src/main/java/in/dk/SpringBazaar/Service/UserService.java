package in.dk.SpringBazaar.Service;

import in.dk.SpringBazaar.Dto.AuthResponse;
import in.dk.SpringBazaar.Dto.RegisterDto;
import in.dk.SpringBazaar.Entity.Role;
import in.dk.SpringBazaar.Entity.User;
import in.dk.SpringBazaar.JwtToken.JwtTokenProvider;
import in.dk.SpringBazaar.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String register(String name, String email, String password, Role role) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User student = new User();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(passwordEncoder.encode(password));
        student.setRole(role);
        userRepository.save(student);

        return "Registration successful";
    }

    public AuthResponse login(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String accessToken = jwtTokenProvider.generateToken(email);
            String refreshToken = jwtTokenProvider.generateRefreshToken(email);
            return new AuthResponse(accessToken, refreshToken);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid email/password");
        }
    }

    public AuthResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);
        return new AuthResponse(newAccessToken, newRefreshToken);
    }

    public RegisterDto getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));
        return new RegisterDto(user.getName(), user.getEmail(), user.getRole());
    }

    public boolean isAdmin(String email) {
        return userRepository.findByEmail(email)
                .map(user -> user.getRole() != null && user.getRole().contains("ADMIN"))
                .orElse(false);
    }
}
