package dreamcompany.service.implementation.friend_request;

import dreamcompany.domain.entity.FriendRequest;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.rest.FriendRequestRestModel;
import dreamcompany.error.InvalidFriendRequestException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.FriendRequestRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.FriendRequestService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FriendRequestServiceTests extends TestBase {

    @Autowired
    FriendRequestService friendRequestService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    FriendRequestRepository requestRepository;

    FriendRequestRestModel friendRequest;

    @Override
    protected void before() {
        friendRequest = new FriendRequestRestModel();
    }

    @Test
    public void send_shouldCreateFriendRequestAndSaveItInDb_whenValidModel() {

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        friendRequest.setSenderUsername(sender.getUsername());
        friendRequest.setReceiverId(receiver.getId());

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(receiver));

        when(requestRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ArgumentCaptor<FriendRequest> argument = ArgumentCaptor.forClass(FriendRequest.class);

        friendRequestService.send(friendRequest);

        verify(requestRepository).save(argument.capture());

        FriendRequest actualSavedRequest = argument.getValue();

        assertEquals(sender.getUsername(), actualSavedRequest.getSender().getUsername());
        assertEquals(sender.getId(), actualSavedRequest.getSender().getId());
        assertEquals(receiver.getUsername(), actualSavedRequest.getReceiver().getUsername());
        assertEquals(receiver.getId(), actualSavedRequest.getReceiver().getId());
        assertEquals(0, actualSavedRequest.getMutualFriends());
    }

    @Test(expected = UserNotFoundException.class)
    public void send_shouldThrowException_whenInvalidSenderUsername() {

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.empty());

        friendRequestService.send(friendRequest);
    }

    @Test(expected = UserNotFoundException.class)
    public void send_shouldThrowException_whenInvalidReceiverId() {

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(new User()));

        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        friendRequestService.send(friendRequest);
    }

    @Test(expected = InvalidFriendRequestException.class)
    public void send_shouldThrowException_whenReceiverHasAlreadySendRequestToSender() {

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());
        FriendRequest oldFriendRequest = new FriendRequest();
        oldFriendRequest.setReceiver(sender);
        sender.getFriendRequests().add(oldFriendRequest);


        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        oldFriendRequest.setSender(receiver);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(receiver));

        friendRequestService.send(friendRequest);
    }

    @Test(expected = InvalidFriendRequestException.class)
    public void send_shouldThrowException_whenReceiverAndSenderAreTheSame() {

        User sender = new User();
        sender.setId("sameId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("sameId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(receiver));

        friendRequestService.send(friendRequest);
    }

    @Test(expected = InvalidFriendRequestException.class)
    public void send_shouldThrowException_whenReceiverAndSenderAreFriends() {

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        receiver.getFriends().add(sender);
        sender.getFriends().add(receiver);

        when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(receiver));

        friendRequestService.send(friendRequest);
    }
}
