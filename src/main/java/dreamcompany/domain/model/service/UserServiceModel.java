package dreamcompany.domain.model.service;

import dreamcompany.domain.entity.Position;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserServiceModel extends BaseServiceModel {

    private String firstName;

    private String lastName;

    private Position position;

    private String imageUrl;

    private String imageId;

    private String email;

    private Integer credits;

    private BigDecimal salary;

    private LocalDateTime hiredOn;

    private String username;

    private String password;

    private TeamServiceModel team;

    private Set<RoleServiceModel> authorities;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public UserServiceModel() {
        authorities = new HashSet<>();
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDateTime getHiredOn() {
        return hiredOn;
    }

    public void setHiredOn(LocalDateTime hiredOn) {
        this.hiredOn = hiredOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleServiceModel> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<RoleServiceModel> authorities) {
        this.authorities = authorities;
    }

    public TeamServiceModel getTeam() {
        return team;
    }

    public void setTeam(TeamServiceModel team) {
        this.team = team;
    }
}
