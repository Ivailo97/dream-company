package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAssignedViewModel {

    private String id;

    private String description;

    private Integer credits;

    private long minutesNeeded;
}
