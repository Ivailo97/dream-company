package dreamcompany.domain.model.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageRestModel {

    private String type;
    private String content;
    private String sender;
    private String imageUrl;
}
