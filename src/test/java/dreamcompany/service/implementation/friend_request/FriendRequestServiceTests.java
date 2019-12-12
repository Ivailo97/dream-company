package dreamcompany.service.implementation.friend_request;

import dreamcompany.domain.entity.FriendRequest;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.rest.FriendRequestRestModel;
import dreamcompany.domain.model.service.FriendRequestServiceModel;
import dreamcompany.error.InvalidFriendRequestException;
import dreamcompany.error.notexist.FriendRequestNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.FriendRequestRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.FriendRequestService;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        User mutualFriend = new User();
        mutualFriend.setId("mutualFriendId");
        mutualFriend.setUsername("mutualFriendUsername");
        mutualFriend.setFriends(new HashSet<>());
        sender.getFriends().add(mutualFriend);

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());
        receiver.getFriends().add(mutualFriend);

        User receiverAnotherFriend = new User();
        receiverAnotherFriend.setUsername("anotherOneFriend");
        receiverAnotherFriend.setFriends(new HashSet<>());
        receiverAnotherFriend.getFriends().add(receiver);
        receiver.getFriends().add(receiverAnotherFriend);

        mutualFriend.getFriends().add(sender);
        mutualFriend.getFriends().add(receiver);

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
        assertEquals(1, actualSavedRequest.getMutualFriends());
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

    @Test
    public void canSendFriendRequest_shouldReturnTrue_whenThereIsNoOtherFriendRequestWithSameSenderAndReceiverAndTheyNotFriendsAndSenderDoesntHaveRequestFromReceiver(){

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        when(userRepository.findByUsername(sender.getUsername()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findByUsername(receiver.getUsername()))
                .thenReturn(Optional.of(receiver));

        when(requestRepository.findAllBySenderUsernameAndReceiverUsername(any(),any()))
                .thenReturn(new ArrayList<>());

        assertTrue(friendRequestService.canSendFriendRequest(receiver.getUsername(),sender.getUsername()));
    }

    @Test
    public void canSendFriendRequest_shouldReturnFalse_whenThereIsOtherFriendRequestWithSameSenderAndReceiver(){

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        when(userRepository.findByUsername(sender.getUsername()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findByUsername(receiver.getUsername()))
                .thenReturn(Optional.of(receiver));

        when(requestRepository.findAllBySenderUsernameAndReceiverUsername(any(),any()))
                .thenReturn(List.of(new FriendRequest()));

        assertFalse(friendRequestService.canSendFriendRequest(receiver.getUsername(),sender.getUsername()));
    }

    @Test
    public void canSendFriendRequest_shouldReturnFalse_whenSenderAndReceiverAreAlreadyFriends(){

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());

        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        when(userRepository.findByUsername(sender.getUsername()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findByUsername(receiver.getUsername()))
                .thenReturn(Optional.of(receiver));

        when(requestRepository.findAllBySenderUsernameAndReceiverUsername(any(),any()))
                .thenReturn(new ArrayList<>());

        assertFalse(friendRequestService.canSendFriendRequest(receiver.getUsername(),sender.getUsername()));
    }

    @Test
    public void canSendFriendRequest_shouldReturnFalse_whenSenderAndReceiverAreTheSame(){

        User sender = new User();
        sender.setId("sameId");
        sender.setUsername("sameUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("sameId");
        receiver.setUsername("sameUsername");
        receiver.setFriends(new HashSet<>());


        when(userRepository.findByUsername(sender.getUsername()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findByUsername(receiver.getUsername()))
                .thenReturn(Optional.of(receiver));

        when(requestRepository.findAllBySenderUsernameAndReceiverUsername(any(),any()))
                .thenReturn(new ArrayList<>());

        assertFalse(friendRequestService.canSendFriendRequest(receiver.getUsername(),sender.getUsername()));
    }

    @Test
    public void canSendFriendRequest_shouldReturnFalse_whenSenderHasRequestFromReceiver(){

        User sender = new User();
        sender.setId("senderId");
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setId("receiverId");
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());
        receiver.setFriendRequests(new HashSet<>());

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(receiver);
        friendRequest.setReceiver(sender);
        sender.getFriendRequests().add(friendRequest);


        when(userRepository.findByUsername(sender.getUsername()))
                .thenReturn(Optional.of(sender));

        when(userRepository.findByUsername(receiver.getUsername()))
                .thenReturn(Optional.of(receiver));

        when(requestRepository.findAllBySenderUsernameAndReceiverUsername(any(),any()))
                .thenReturn(new ArrayList<>());

        assertFalse(friendRequestService.canSendFriendRequest(receiver.getUsername(),sender.getUsername()));
    }

    @Test(expected = UserNotFoundException.class)
    public void canSendFriendRequest_shouldThrowException_whenInvalidReceiverUsername(){

        when(userRepository.findByUsername("receiverUsername"))
                .thenReturn(Optional.empty());


        friendRequestService.canSendFriendRequest("receiverUsername","senderUsername");

    }

    @Test(expected = UserNotFoundException.class)
    public void canSendFriendRequest_shouldThrowException_whenInvalidSenderUsername(){

        when(userRepository.findByUsername("senderUsername"))
                .thenReturn(Optional.of(new User()));

        when(userRepository.findByUsername("senderUsername"))
                .thenReturn(Optional.empty());

        friendRequestService.canSendFriendRequest("receiverUsername","senderUsername");
    }

    @Test
    public void findRequestsForUser_shouldFindFriendRequestInDbByReceiver_whenAny(){

        List<FriendRequest> requests = List.of(new FriendRequest(),new FriendRequest());

        when(requestRepository.findAllByReceiverUsername(any()))
                .thenReturn(requests);

        List<FriendRequestServiceModel> returned = friendRequestService.findRequestsForUser("receiverUsername");

        assertEquals(requests.size(),returned.size());
    }

    @Test
    public void findAllBySenderAndReceiver_shouldFindFriendRequestsInDbBySenderAndReceiverUsername_whenAny(){

        List<FriendRequest> requests = List.of(new FriendRequest(),new FriendRequest());

        when(requestRepository.findAllBySenderUsernameAndReceiverUsername(any(),any()))
                .thenReturn(requests);

        List<FriendRequestServiceModel> returned = friendRequestService.findAllBySenderAndReceiver("senderUsername","receiverUsername");

        assertEquals(requests.size(),returned.size());
    }

    @Test
    public void accept_shouldAddRequestSenderToReceiverFriendsAndRequestReceiverToSenderFriends_whenValidRequestId(){

        User sender = new User();
        sender.setUsername("senderUsername");
        sender.setFriends(new HashSet<>());
        sender.setFriendRequests(new HashSet<>());

        User receiver = new User();
        receiver.setUsername("receiverUsername");
        receiver.setFriends(new HashSet<>());
        receiver.setFriendRequests(new HashSet<>());

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);

        when(requestRepository.findById(any()))
                .thenReturn(Optional.of(friendRequest));

        when(userRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        friendRequestService.accept("friendRequestId");

        verify(userRepository,times(2)).save(any());
        verify(requestRepository).delete(any());
        assertEquals(1,sender.getFriends().size());
        assertEquals(1,receiver.getFriends().size());
        assertEquals("senderUsername",receiver.getFriends().iterator().next().getUsername());
        assertEquals("receiverUsername",sender.getFriends().iterator().next().getUsername());
    }

    @Test(expected = FriendRequestNotFoundException.class)
    public void accept_shouldThrowException_whenInvalidFriendRequestId(){

        when(requestRepository.findById(any()))
                .thenReturn(Optional.empty());

        friendRequestService.accept("friendRequestId");
    }

    @Test
    public void decline_shouldDeleteRequest_whenValidRequestId(){

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setId("reqId");

        when(requestRepository.findById(any()))
                .thenReturn(Optional.of(friendRequest));

        ArgumentCaptor<FriendRequest> argument = ArgumentCaptor.forClass(FriendRequest.class);

        friendRequestService.decline("reqId");

        verify(requestRepository).delete(argument.capture());

        assertEquals(friendRequest.getId(),argument.getValue().getId());
    }

    @Test(expected = FriendRequestNotFoundException.class)
    public void decline_shouldThrowException_whenInvalidRequestId(){

        when(requestRepository.findById(any()))
                .thenReturn(Optional.empty());

        friendRequestService.decline("reqId");
    }
}
