package campusmarketplace.controller;

import campusmarketplace.dto.ChangePasswordRequest;
import campusmarketplace.dto.UpdateProfileRequest;
import campusmarketplace.dto.UserProfileResponse;
import campusmarketplace.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Identity always comes from the JWT (authentication.getName()),
     * never from a client-supplied email — otherwise anyone could
     * read or edit someone else's profile.
     */
    @GetMapping("/me")
    public UserProfileResponse getMyProfile(Authentication authentication) {

        return userService.getProfile(authentication.getName());
    }

    @PutMapping("/me")
    public UserProfileResponse updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {

        return userService.updateProfile(authentication.getName(), request);
    }

    @PutMapping("/me/password")
    public String changeMyPassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        return userService.changePassword(authentication.getName(), request);
    }
}