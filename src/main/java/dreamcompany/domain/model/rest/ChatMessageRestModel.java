package dreamcompany.domain.model.rest;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageRestModel {

    private String type;
    private String content;
    private String sender;
    private String imageUrl;
}
