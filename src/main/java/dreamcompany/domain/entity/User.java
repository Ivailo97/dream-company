package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "photo_url")
    private String imageUrl;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "credits", nullable = false)
    private Integer credits;

    @Column(name = "salary", nullable = false)
    private BigDecimal salary;

    @Column(name = "hired_on", nullable = false)
    private LocalDateTime hiredOn;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private Position position;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Team team;

    @OneToMany(mappedBy = "employee")
    private Set<Task> assignedTasks;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    private Set<Role> authorities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_friends",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id",referencedColumnName = "id")
    )
    private Set<User> friends;

    @OneToMany(mappedBy = "sender")
    private Set<FriendRequest> friendRequests;

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return true;
    }
}
