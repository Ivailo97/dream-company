package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageViewModel {

    private String type;

    private String sender;

    private String content;

    private String imageUrl;

    private LocalDateTime createdOn;
}
