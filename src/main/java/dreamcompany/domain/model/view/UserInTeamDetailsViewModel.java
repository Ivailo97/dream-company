package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserInTeamDetailsViewModel {

    private String imageUrl;

    private String fullName;

    private String position;

    private LocalDateTime hiredOn;
}
