package campusmarketplace.service;

import campusmarketplace.dto.LoginResponse;
import campusmarketplace.dto.LoginRequest;
import campusmarketplace.dto.RegisterRequest;
import campusmarketplace.entity.User;
import campusmarketplace.repository.UserRepository;
import org.springframework.stereotype.Service;
import campusmarketplace.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    private static final java.util.Set<String> ALLOWED_SELF_REGISTER_ROLES = java.util.Set.of("BUYER", "SELLER");

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        String requestedRole = request.getRole() == null
                ? ""
                : request.getRole().trim().toUpperCase();

        if (!ALLOWED_SELF_REGISTER_ROLES.contains(requestedRole)) {
            throw new BadRequestException("Role must be one of: " + ALLOWED_SELF_REGISTER_ROLES);
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(requestedRole);

        userRepository.save(user);

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return LoginResponse.builder().message("User not found").build();
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return LoginResponse.builder().message("Invalid password").build();
        }
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return LoginResponse.builder()
                .message("Login successful")
                .token(token)
                .role(user.getRole())
                .build();
    }
}