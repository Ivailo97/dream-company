package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDeleteViewModel {

    private String id;

    private String name;

    private String description;

    private String requiredPosition;

    private Integer credits;

    //id
    private String project;
}
