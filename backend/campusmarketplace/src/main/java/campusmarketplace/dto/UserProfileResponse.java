package campusmarketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * What we send back to the client for a user's profile.
 * Deliberately excludes the password hash — never expose it,
 * which is why we return this DTO instead of the User entity.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

    private String name;
    private String email;
    private String role;
    private String shippingAddress;
}