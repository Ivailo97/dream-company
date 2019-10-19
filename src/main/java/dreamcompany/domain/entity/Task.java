package dreamcompany.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {

    private String name;

    private String description;

    private Status status;

    private LocalDateTime createdOn;

    private Position requiredPosition;

    private Integer credits;

    private Project project;

    private long minutesNeeded;

    private User employee;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinutesNeeded(long minutesNeeded) {
        this.minutesNeeded = minutesNeeded;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setRequiredPosition(Position requiredPosition) {
        this.requiredPosition = requiredPosition;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "minutes_to_accomplish", nullable = false)
    public long getMinutesNeeded() {
        return minutesNeeded;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    @Column(name = "created_on", nullable = false)
    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    @Column(name = "required_position", nullable = false)
    @Enumerated(EnumType.STRING)
    public Position getRequiredPosition() {
        return requiredPosition;
    }

    @Column(name = "credits", nullable = false)
    public Integer getCredits() {
        return credits;
    }

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    public Project getProject() {
        return project;
    }

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    public User getEmployee() {
        return employee;
    }
}
