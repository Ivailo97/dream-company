package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task extends Assignment {

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "required_position", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position requiredPosition;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    @Column(name = "minutes_to_accomplish", nullable = false)
    private long minutesNeeded;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private User employee;
}
