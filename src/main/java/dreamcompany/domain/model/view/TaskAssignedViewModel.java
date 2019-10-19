package dreamcompany.domain.model.view;

public class TaskAssignedViewModel {

    private String id;

    private String description;

    private Integer credits;

    private long minutesNeeded;

    public String getId() {
        return id;
    }

    public long getMinutesNeeded() {
        return minutesNeeded;
    }

    public void setMinutesNeeded(long minutesNeeded) {
        this.minutesNeeded = minutesNeeded;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
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
}
