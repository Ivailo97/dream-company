package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskFetchViewModel {

    private String id;

    private String name;

    private LocalDateTime createdOn;
}
