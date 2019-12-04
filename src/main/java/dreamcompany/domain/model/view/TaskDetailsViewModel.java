package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDetailsViewModel {

    private String description;

    private Integer credits;

    private String requiredPosition;

    private String project;

    private long minutesNeeded;
}
