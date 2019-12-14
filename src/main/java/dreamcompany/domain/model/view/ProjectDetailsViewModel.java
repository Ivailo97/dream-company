package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ProjectDetailsViewModel {

    private String id;

    private String name;

    private String description;

    private TeamAllViewModel team;

    private BigDecimal reward;

    private Set<String> tasks;
}
