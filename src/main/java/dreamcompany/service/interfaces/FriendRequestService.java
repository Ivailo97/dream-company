package dreamcompany.service.interfaces;

import dreamcompany.domain.model.rest.FriendRequestRestModel;
import dreamcompany.domain.model.service.FriendRequestServiceModel;

import java.util.List;


public interface FriendRequestService {

    void send(FriendRequestRestModel friendRequest);

    boolean canSendFriendRequest(String receiverUsername,String loggedUserUsername);

    List<FriendRequestServiceModel> findRequestsForUser(String receiverUsername);

    void accept(String id);

    void decline(String id);
}
