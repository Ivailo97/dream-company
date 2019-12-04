package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "offices")
public class Office extends BaseEntity {

    @Column(name = "address", nullable = false, unique = true)
    private String address;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "town", nullable = false)
    private String town;

    @Column(name = "country", nullable = false)
    private String country;

    @OneToMany(mappedBy = "office")
    private Set<Team> teams;
}