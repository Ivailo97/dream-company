package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserAssignTaskViewModel {

    private String id;

    private String username;

    private String imageUrl;

    private String position;

    private String fullName;

    private LocalDateTime hiredOn;
}
