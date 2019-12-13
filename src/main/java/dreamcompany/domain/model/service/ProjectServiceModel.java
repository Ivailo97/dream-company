package dreamcompany.domain.model.service;

import dreamcompany.domain.enumeration.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class ProjectServiceModel extends BaseServiceModel {

    private String name;

    private String description;

    private Status status;

    private BigDecimal reward;

    private Set<TaskServiceModel> tasks;

    private TeamServiceModel team;
}
