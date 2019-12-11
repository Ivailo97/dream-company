package dreamcompany.domain.model.service;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskServiceModel extends BaseServiceModel {

    private String name;

    private String description;

    private Status status;

    private LocalDateTime createdOn;

    private Position requiredPosition;

    private Integer credits;

    //project id
    private String project;

    private long minutesNeeded;
}
