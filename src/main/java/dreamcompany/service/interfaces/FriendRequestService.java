package dreamcompany.service.interfaces;

import dreamcompany.domain.entity.FriendRequest;
import dreamcompany.domain.model.rest.FriendRequestRestModel;
import dreamcompany.domain.model.service.FriendRequestServiceModel;

import java.util.List;


public interface FriendRequestService {

    void send(FriendRequestRestModel friendRequest);

    boolean canSendFriendRequest(String receiverUsername,String loggedUserUsername);

    List<FriendRequestServiceModel> findRequestsForUser(String receiverUsername);

    List<FriendRequestServiceModel> findAllBySenderAndReceiver(String sender,String receiver);

    void accept(String id);

    void decline(String id);
}
