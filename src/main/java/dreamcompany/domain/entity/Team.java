package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class Team extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "profit", nullable = false)
    private BigDecimal profit;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Column(name = "logo_id", nullable = false)
    private String logoId;

    @ManyToOne
    @JoinColumn(name = "office_id", referencedColumnName = "id")
    private Office office;

    @OneToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "prev_position_of_team_lead", nullable = false)
    @Enumerated(EnumType.STRING)
    private Position teamLeaderPreviousPosition;

    @OneToMany(mappedBy = "team")
    private Set<User> employees;
}
