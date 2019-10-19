package dreamcompany.domain.model.view;

public class TaskDetailsViewModel {

    private String description;

    private Integer credits;

    private String requiredPosition;

    private String project;

    private long minutesNeeded;

    public long getMinutesNeeded() {
        return minutesNeeded;
    }

    public void setMinutesNeeded(long minutesNeeded) {
        this.minutesNeeded = minutesNeeded;
    }

    public String getDescription() {
        return description;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String projectName) {
        this.project = projectName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getRequiredPosition() {
        return requiredPosition;
    }

    public void setRequiredPosition(String requiredPosition) {
        this.requiredPosition = requiredPosition;
    }
}
