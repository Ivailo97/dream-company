package dreamcompany.domain.model.view;

import java.math.BigDecimal;
import java.util.Set;

public class ProjectDetailsViewModel {

    private String description;

    private BigDecimal reward;

    private Set<String> tasks;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
