package dreamcompany.domain.model.view;

import java.time.LocalDateTime;

public class UserAssignTaskViewModel {

    private String id;

    private String imageUrl;

    private String position;

    private String fullName;

    private LocalDateTime hiredOn;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getHiredOn() {
        return hiredOn;
    }

    public void setHiredOn(LocalDateTime hiredOn) {
        this.hiredOn = hiredOn;
    }
}
