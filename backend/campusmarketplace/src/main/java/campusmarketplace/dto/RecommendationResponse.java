package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendationResponse {

    private Long id;

    private String title;

    private String category;

    private Double price;
}