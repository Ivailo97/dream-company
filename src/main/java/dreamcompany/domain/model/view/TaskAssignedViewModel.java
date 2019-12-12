package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAssignedViewModel {

    private String id;

    private String name;

    private String description;

    private Integer credits;

    private long minutesNeeded;
}
