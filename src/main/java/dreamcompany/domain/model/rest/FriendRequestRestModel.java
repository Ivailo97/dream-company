package dreamcompany.domain.model.rest;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FriendRequestRestModel {

    private String senderUsername;

    private String receiverId;
}
