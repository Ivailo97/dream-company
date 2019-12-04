package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TeamAllViewModel {

    private String id;

    private String logoUrl;

    private String name;

    private BigDecimal profit;
}
