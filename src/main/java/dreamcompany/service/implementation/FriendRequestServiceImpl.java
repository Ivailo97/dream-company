package dreamcompany.service.implementation;

import dreamcompany.domain.entity.BaseEntity;
import dreamcompany.domain.entity.FriendRequest;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.rest.FriendRequestRestModel;
import dreamcompany.domain.model.service.FriendRequestServiceModel;
import dreamcompany.error.InvalidFriendRequestException;
import dreamcompany.error.notexist.FriendRequestNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.FriendRequestRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.FriendRequestService;
import dreamcompany.util.MappingConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dreamcompany.common.GlobalConstants.*;

@Service
@AllArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository requestRepository;

    private final MappingConverter converter;

    private final UserRepository userRepository;

    @Override
    public void send(FriendRequestRestModel model) {

        User sender = userRepository.findByUsername(model.getSenderUsername())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        User receiver = userRepository.findById(model.getReceiverId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        if (senderHasAlreadyRequestFromReceiver(sender, receiver)) {
            throw new InvalidFriendRequestException(INVALID_REQUEST_USER_HAS_ALREADY_SEND);
        }

        if (senderAndReceiverAreTheSame(sender, receiver)) {
            throw new InvalidFriendRequestException(INVALID_FRIEND_REQUEST_MESSAGE);
        }

        if (senderAndReceiverAreAlreadyFriends(sender, receiver)) {
            throw new InvalidFriendRequestException(INVALID_REQUEST_MESSAGE);
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setMutualFriends(calculateMutualFriends(sender, receiver));
        friendRequest.setCreatedOn(LocalDateTime.now());

        requestRepository.save(friendRequest);
    }

    private long calculateMutualFriends(User sender, User receiver) {

        Map<String, Integer> namesAndCount = new HashMap<>();

        sender.getFriends().forEach(f -> {
            if (!namesAndCount.containsKey(f.getUsername())) {
                namesAndCount.put(f.getUsername(), 0);
            }
            namesAndCount.put(f.getUsername(), namesAndCount.get(f.getUsername()) + 1);
        });

        receiver.getFriends().forEach(f -> {
            if (!namesAndCount.containsKey(f.getUsername())) {
                namesAndCount.put(f.getUsername(), 0);
            }
            namesAndCount.put(f.getUsername(), namesAndCount.get(f.getUsername()) + 1);
        });

        return namesAndCount.entrySet().stream().filter(e -> e.getValue().compareTo(1) > 0).count();
    }

    @Override
    public boolean canSendFriendRequest(String receiverUsername, String loggedUserUsername) {

        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        User sender = userRepository.findByUsername(loggedUserUsername)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        return requestRepository.findAllBySenderUsernameAndReceiverUsername(loggedUserUsername, receiverUsername).size() == 0
                && !senderAndReceiverAreAlreadyFriends(sender,receiver)
                && !receiverUsername.equals(loggedUserUsername)
                && !senderHasAlreadyRequestFromReceiver(sender, receiver);
    }

    private boolean senderHasAlreadyRequestFromReceiver(User sender, User receiver) {

        return sender.getFriendRequests().stream()
                .anyMatch(r -> r.getSender().getUsername().equals(receiver.getUsername()));
    }

    @Override
    public List<FriendRequestServiceModel> findRequestsForUser(String receiverUsername) {
        return requestRepository.findAllByReceiverUsername(receiverUsername).stream()
                .map(r -> converter.map(r, FriendRequestServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendRequestServiceModel> findAllBySenderAndReceiver(String sender, String receiver) {
        return requestRepository.findAllBySenderUsernameAndReceiverUsername(sender, receiver)
                .stream().map(r -> converter.map(r, FriendRequestServiceModel.class))
                .collect(Collectors.toList());
    }

    private boolean senderAndReceiverAreAlreadyFriends(User sender, User receiver) {
        return sender.getFriends().stream().map(BaseEntity::getId)
                .anyMatch(f -> f.equals(receiver.getId()));
    }

    private boolean senderAndReceiverAreTheSame(User sender, User receiver) {
        return sender.getId().equals(receiver.getId());
    }

    @Override
    public void accept(String id) {

        FriendRequest friendRequest = requestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestNotFoundException(FRIEND_REQUEST_NOT_FOUND_MESSAGE));

        User sender = friendRequest.getSender();
        User receiver = friendRequest.getReceiver();

        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);

        requestRepository.delete(friendRequest);
    }

    @Override
    public void decline(String id) {

        FriendRequest friendRequest = requestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestNotFoundException(FRIEND_REQUEST_NOT_FOUND_MESSAGE));

        requestRepository.delete(friendRequest);
    }
}
