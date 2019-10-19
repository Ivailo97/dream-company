package dreamcompany.domain.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {

    private String firstName;

    private String lastName;

    private String imageUrl;

    private String imageId;

    private String email;

    private Integer credits;

    private BigDecimal salary;

    private LocalDateTime hiredOn;

    private Position position;

    private String username;

    private String password;

    private Team team;

    private Set<Task> assignedTasks;

    private Set<Role> authorities;

    public User() {
    }

    @Override
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id")
    )
    public Set<Role> getAuthorities() {
        return this.authorities;
    }

    @Override
    @Column(name = "password", nullable = false)
    public String getPassword() {
        return this.password;
    }

    @Override
    @Column(name = "username", nullable = false, unique = true, updatable = false)
    public String getUsername() {
        return this.username;
    }


    @Column(name = "first_name", nullable = false)
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name", nullable = false)
    public String getLastName() {
        return lastName;
    }

    @Column(name = "photo_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    @Column(name = "credits", nullable = false)
    public Integer getCredits() {
        return credits;
    }

    @Column(name = "salary", nullable = false)
    public BigDecimal getSalary() {
        return salary;
    }

    @Column(name = "hired_on", nullable = false)
    public LocalDateTime getHiredOn() {
        return hiredOn;
    }

    @Column(name = "image_id")
    public String getImageId() {
        return imageId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    public Position getPosition() {
        return position;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    public Team getTeam() {
        return team;
    }

    @OneToMany(mappedBy = "employee")
    public Set<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setHiredOn(LocalDateTime hiredOn) {
        this.hiredOn = hiredOn;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setAssignedTasks(Set<Task> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}
