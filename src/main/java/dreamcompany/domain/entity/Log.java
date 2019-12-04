package dreamcompany.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "logs")
public class Log extends BaseEntity {

    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "event", nullable = false, columnDefinition = "text")
    private String description;
}
