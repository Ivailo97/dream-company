package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ProjectDeleteViewModel {

    private String id;

    private String name;

    private String description;

    private String status;

    private BigDecimal reward;

    private Set<String> tasks;
}
