package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewRequest {

    private String userEmail;

    private Integer rating;

    private String comment;
}