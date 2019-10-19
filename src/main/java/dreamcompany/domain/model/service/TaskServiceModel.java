package dreamcompany.domain.model.service;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Status;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

public class TaskServiceModel extends BaseServiceModel {

    private String name;

    private String description;

    private Status status;

    private LocalDateTime createdOn;

    private Position requiredPosition;

    private Integer credits;

    private String project;

    private long minutesNeeded;

    public void setMinutesNeeded(long minutesNeeded) {
        this.minutesNeeded = minutesNeeded;
    }

    public long getMinutesNeeded() {
        return minutesNeeded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public Position getRequiredPosition() {
        return requiredPosition;
    }

    public void setRequiredPosition(Position requiredPosition) {
        this.requiredPosition = requiredPosition;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
