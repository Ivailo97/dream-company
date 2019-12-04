package dreamcompany.domain.model.service;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FriendRequestServiceModel extends BaseServiceModel {

    private String senderFirstName;

    private String senderLastName;

    private String senderImageUrl;

    private long mutualFriends;

    private LocalDateTime createdOn;
}
