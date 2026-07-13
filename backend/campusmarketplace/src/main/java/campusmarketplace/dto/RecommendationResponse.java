package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class RecommendationResponse {

    private UUID id;

    private String title;

    private String category;

    private Double price;
}