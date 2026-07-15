package campusmarketplace.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Fields a user is allowed to change about themselves.
 * Note: email and role are NOT here — a user must not be able to
 * change their own identity or promote themselves to a different role.
 */
public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String shippingAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}