package dreamcompany.domain.model.binding;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProjectCreateBindingModel {

    private String name;

    private String description;

    private BigDecimal reward;
}
