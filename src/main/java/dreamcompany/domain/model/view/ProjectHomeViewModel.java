package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProjectHomeViewModel {

    private String id;

    private String name;

    private String description;

    private BigDecimal reward;
}
