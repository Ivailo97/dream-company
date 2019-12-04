package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "projects")
public class Project extends Assignment {

    @Column(name = "reward", nullable = false)
    private BigDecimal reward;

    @OneToOne(mappedBy = "project")
    private Team team;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL)
    private Set<Task> tasks;
}
