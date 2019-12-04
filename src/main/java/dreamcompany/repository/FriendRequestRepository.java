package dreamcompany.repository;

import dreamcompany.domain.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, String> {

    List<FriendRequest> findAllBySenderUsernameAndReceiverUsername(String senderUsername, String receiverUsername);

    List<FriendRequest> findAllByReceiverUsername(String receiverUsername);
}
