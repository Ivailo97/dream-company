package dreamcompany.domain.model.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LogServiceModel extends BaseServiceModel {

    private LocalDateTime createdOn;
    private String username;
    private String description;
}
