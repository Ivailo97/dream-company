package dreamcompany.domain.model.view;

public class TaskDeleteViewModel {

    private String id;

    private String name;

    private String description;

    private String requiredPosition;

    private Integer credits;

    //id
    private String project;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
