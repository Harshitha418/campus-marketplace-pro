package campusmarketplace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopProductResponse {

    private String title;

    private Long totalSold;
}