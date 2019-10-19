package dreamcompany.domain.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project extends BaseEntity {

    private String name;

    private String description;

    private Status status;

    private BigDecimal reward;

    private Team team;

    private Set<Task> tasks;

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

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

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    @Column(name = "description", nullable = false, columnDefinition = "text")
    public String getDescription() {
        return description;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
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
