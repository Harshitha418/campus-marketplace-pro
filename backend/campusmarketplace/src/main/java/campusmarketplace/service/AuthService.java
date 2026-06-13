package campusmarketplace.service;

import campusmarketplace.dto.LoginResponse;
import campusmarketplace.dto.LoginRequest;
import campusmarketplace.dto.RegisterRequest;
import campusmarketplace.entity.User;
import campusmarketplace.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return "Email already exists";
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        userRepository.save(user);

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return new LoginResponse("User not found", null);
        }
        if (!user.getPassword().equals(request.getPassword())) {
            return new LoginResponse("Invalid password", null);
        }
        String token = jwtService.generateToken(user.getEmail());
        return new LoginResponse(
                "Login successful",
                token);
    }
}
