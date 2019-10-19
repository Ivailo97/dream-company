package dreamcompany.domain.model.service;

import dreamcompany.domain.entity.Status;

import java.math.BigDecimal;
import java.util.Set;

public class ProjectServiceModel extends BaseServiceModel {

    private String name;

    private String description;

    private Status status;

    private BigDecimal reward;

    private Set<TaskServiceModel> tasks;

    private TeamServiceModel team;

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getReward() {
        return reward;
    }

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public Set<TaskServiceModel> getTasks() {
        return tasks;
    }

    public void setTasks(Set<TaskServiceModel> tasks) {
        this.tasks = tasks;
    }

    public TeamServiceModel getTeam() {
        return team;
    }

    public void setTeam(TeamServiceModel team) {
        this.team = team;
    }
}
