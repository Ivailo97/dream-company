package dreamcompany.domain.model.service;

import java.time.LocalDateTime;

public class LogServiceModel extends BaseServiceModel {

    private LocalDateTime createdOn;
    private String username;
    private String description;

    public LogServiceModel() {
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
