package dreamcompany.domain.model.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

public class TaskCreateBindingModel {

    private String name;

    private String description;

    private String requiredPosition;

    private Integer credits;

    //id
    private String project;

    private long minutesNeeded;

    public void setMinutesNeeded(long minutesNeeded) {
        this.minutesNeeded = minutesNeeded;
    }

    @Min(value = 1,message = "Minutes cant be less than 1")
    @Max(value = 60,message = "Minutes cant be more than 60")
    public long getMinutesNeeded() {
        return minutesNeeded;
    }

    @NotEmpty(message = "Name cant be empty")
    @NotNull(message = "Name cant be null")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Description cant be empty")
    @NotNull(message = "Description cant be null")
    @Length(min = 10, max = 300 , message = "Length must be between 10 and 300 symbols long")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotEmpty(message = "Position cant be empty")
    @NotNull(message = "Position cant be null")
    public String getRequiredPosition() {
        return requiredPosition;
    }

    public void setRequiredPosition(String requiredPosition) {
        this.requiredPosition = requiredPosition;
    }

    @NotNull(message = "Credits cant be null")
    @Min(value = 10,message = "Credits must be at least 10")
    @Max(value = 20,message = "Credits must be not more than 20")
    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    @NotNull(message = "Project cant be null")
    @NotEmpty(message = "Project cant be empty")
    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
