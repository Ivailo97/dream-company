package dreamcompany.domain.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends Assignment {

    private BigDecimal reward;

    private Team team;

    private Set<Task> tasks;

    public void setReward(BigDecimal reward) {
        this.reward = reward;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @OneToOne(mappedBy = "project")
    public Team getTeam() {
        return team;
    }

    @Column(name = "reward", nullable = false)
    public BigDecimal getReward() {
        return reward;
    }

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    public Set<Task> getTasks() {
        return tasks;
    }
}
