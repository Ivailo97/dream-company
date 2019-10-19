package dreamcompany.domain.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "teams")
public class Team extends BaseEntity {

    private String name;

    private BigDecimal profit;

    private Office office;

    private Project project;

    private Position teamLeaderPreviousPosition;

    private Set<User> employees;

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "profit", nullable = false)
    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    public Office getOffice() {
        return office;
    }

    @OneToOne
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @OneToMany(mappedBy = "team")
    public Set<User> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<User> employees) {
        this.employees = employees;
    }

    @Column(name = "prev_position_of_team_lead", nullable = false)
    @Enumerated(EnumType.STRING)
    public Position getTeamLeaderPreviousPosition() {
        return teamLeaderPreviousPosition;
    }

    public void setTeamLeaderPreviousPosition(Position teamLeaderPreviousPosition) {
        this.teamLeaderPreviousPosition = teamLeaderPreviousPosition;
    }
}
