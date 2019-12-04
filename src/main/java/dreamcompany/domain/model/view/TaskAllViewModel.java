package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskAllViewModel {

    private String id;

    private String name;

    private LocalDateTime createdOn;

    private String status;
}
