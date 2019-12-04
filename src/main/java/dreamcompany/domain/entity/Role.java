package dreamcompany.domain.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(name = "authority", nullable = false, unique = true, updatable = false)
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
