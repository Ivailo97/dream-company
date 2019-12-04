package dreamcompany.domain.model.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageServiceModel extends BaseServiceModel {

    private String type;
    private String content;
    private String sender;
    private String imageUrl;
    private LocalDateTime createdOn;
}
