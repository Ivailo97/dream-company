package dreamcompany.domain.model.view;

import dreamcompany.domain.entity.Status;

import java.math.BigDecimal;
import java.util.Set;

public class ProjectDeleteViewModel {

    private String id;

    private String name;

    private String description;

    private String status;

    private BigDecimal reward;

    private Set<String> tasks;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public Set<String> getTasks() {
        return tasks;
    }

    public void setTasks(Set<String> tasks) {
        this.tasks = tasks;
    }
}
