package dreamcompany.domain.model.binding;

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

    public long getMinutesNeeded() {
        return minutesNeeded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredPosition() {
        return requiredPosition;
    }

    public void setRequiredPosition(String requiredPosition) {
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
}
