package campusmarketplace.service;

import campusmarketplace.dto.ChangePasswordRequest;
import campusmarketplace.dto.UpdateProfileRequest;
import campusmarketplace.dto.UserProfileResponse;
import campusmarketplace.entity.User;
import campusmarketplace.exception.BadRequestException;
import campusmarketplace.exception.ResourceNotFoundException;
import campusmarketplace.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Look up the logged-in user, or fail loudly if the token points at nothing.
     */
    private User findByEmail(String email) {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return user;
    }

    public UserProfileResponse getProfile(String email) {

        User user = findByEmail(email);

        return UserProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .shippingAddress(user.getShippingAddress())
                .build();
    }

    public UserProfileResponse updateProfile(
            String email,
            UpdateProfileRequest request) {

        User user = findByEmail(email);

        user.setName(request.getName());
        user.setShippingAddress(request.getShippingAddress());

        userRepository.save(user);

        return UserProfileResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .shippingAddress(user.getShippingAddress())
                .build();
    }

    public String changePassword(
            String email,
            ChangePasswordRequest request) {

        User user = findByEmail(email);

        // Verify the current password before allowing a change.
        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            throw new BadRequestException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Password updated successfully";
    }
}