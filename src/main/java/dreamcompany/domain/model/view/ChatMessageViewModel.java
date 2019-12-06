package dreamcompany.domain.model.view;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ChatMessageViewModel {

    private String type;

    private String sender;

    private String content;

    private String imageUrl;

    private Date createdOn;
}
