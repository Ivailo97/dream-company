package dreamcompany.domain.model.view;

import java.time.LocalDateTime;

public class UserInTeamDetailsViewModel {

    private String imageUrl;

    private String fullName;

    private String position;

    private LocalDateTime hiredOn;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDateTime getHiredOn() {
        return hiredOn;
    }

    public void setHiredOn(LocalDateTime hiredOn) {
        this.hiredOn = hiredOn;
    }
}
