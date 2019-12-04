package dreamcompany.domain.model.binding;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskEditBindingModel {

    private String id;

    private String name;

    private String description;

    private String requiredPosition;

    private Integer credits;

    //id
    private String project;

    private long minutesNeeded;
}
