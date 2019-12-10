package dreamcompany.service.implementation.user;

import dreamcompany.domain.entity.FriendRequest;
import dreamcompany.domain.entity.User;
import dreamcompany.error.InvalidOperationException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class UserServiceAdditionalTests extends TestBase {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    User principal;

    @Override
    protected void before() {
        principal = new User();
        principal.setUsername("Dmitri");
        principal.setFriends(new HashSet<>());
    }

    @Test
    public void canRemoveFriend_shouldReturnTrue_whenLoggedUserHasThatFriendInList() {

        User friend = new User();
        friend.setUsername("Spas");

        principal.getFriends().add(friend);

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        //Act and assert
        assertTrue(userService.canRemoveFriend("Dmitri", "Spas"));
    }

    @Test
    public void canRemoveFriend_shouldReturnFalse_whenLoggedUserDoesntHaveThatFriendInList() {

        User friend = new User();
        friend.setUsername("Vladii");

        principal.getFriends().add(friend);

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        //Act and assert
        assertFalse(userService.canRemoveFriend("Dmitri", "Vlad"));
    }

    @Test
    public void canRemoveFriend_shouldReturnFalse_whenTryingRemoveYourselfFromList() {

        String loggedUserUsername = "Dmitri";
        String friendUsername = "Dmitri";

        User principal = new User();
        principal.setUsername(loggedUserUsername);

        when(userRepository.findByUsername(loggedUserUsername))
                .thenReturn(Optional.of(principal));

        //Act and assert
        assertFalse(userService.canRemoveFriend(loggedUserUsername, friendUsername));
    }

    @Test(expected = UsernameNotFoundException.class)
    public void canRemoveFriend_shouldThrowException_whenCalledWithNonExistingLoggedUser() {

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.empty());

        //Act
        userService.canRemoveFriend("Dmitri", "Vlad");
    }

    @Test
    public void removeFriend_shouldRemoveFriend_whenFriendExistsInUserList() {

        User friend = new User();
        friend.setUsername("Vlad");
        friend.setFriends(new HashSet<>());
        principal.getFriends().add(friend);

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        when(userRepository.findByUsername("Vlad"))
                .thenReturn(Optional.of(friend));

        when(userRepository.save(any()))
                .thenReturn(null);

        //Act
        userService.removeFriend(principal.getUsername(), "Vlad");
        assertEquals(0, principal.getFriends().size());
        assertEquals(0, friend.getFriends().size());
    }

    @Test(expected = InvalidOperationException.class)
    public void removeFriend_shouldThrowException_whenTryingRemoveYourself() {

        userService.removeFriend("Dmitri", "Dmitri");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void removeFriend_shouldThrowException_whenCalledWithInvalidUsernameAtFirstParameter() {

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.empty());

        userService.removeFriend("Dmitri", "Vlad");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void removeFriend_shouldThrowException_whenCalledWithInvalidUsernameAtSecondParameter() {

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        when(userRepository.findByUsername("Vlad"))
                .thenReturn(Optional.empty());

        userService.removeFriend("Dmitri", "Vlad");
    }

    @Test(expected = InvalidOperationException.class)
    public void removeFriend_shouldThrowException_whenTryingRemoveFriendWhichIsNotInYourList() {

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        User friend = new User();
        friend.setUsername("Vlad");
        friend.setFriends(new HashSet<>());

        when(userRepository.findByUsername("Vlad"))
                .thenReturn(Optional.of(friend));

        userService.removeFriend("Dmitri", "Vlad");
    }

    @Test
    public void canAcceptRequest_shouldReturnTrue_whenLoggedUserHasRequestFromTheSender(){

        principal.setFriendRequests(new HashSet<>());
        FriendRequest friendRequest = new FriendRequest();

        User sender = new User();
        sender.setUsername("pesho");

        friendRequest.setSender(sender);
        principal.getFriendRequests().add(friendRequest);

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        when(userRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(sender));

        assertTrue(userService.canAcceptRequest("pesho","Dmitri"));
    }

    @Test(expected = UserNotFoundException.class)
    public void canAcceptRequest_shouldThrowException_whenInvalidSenderName(){

        when(userRepository.findByUsername("pesho"))
                .thenReturn(Optional.empty());

        userService.canAcceptRequest("pesho","Dmitri");
    }

    @Test(expected = UserNotFoundException.class)
    public void canAcceptRequest_shouldThrowException_whenInvalidLoggedUserUsername(){

        when(userRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(new User()));

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.empty());

        userService.canAcceptRequest("pesho","Dmitri");
    }

    @Test
    public void canAcceptRequest_shouldReturnFalse_whenSenderNameAndLoggedUserUsernameAreTheSame(){

        principal.setFriendRequests(new HashSet<>());

        when(userRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(principal));

        assertFalse(userService.canAcceptRequest("pesho","pesho"));
    }

    @Test
    public void canAcceptRequest_shouldReturnFalse_whenReceiverDoesntHaveRequestFromSender(){

        principal.setFriendRequests(new HashSet<>());

        User sender = new User();
        sender.setUsername("pesho");

        when(userRepository.findByUsername("Dmitri"))
                .thenReturn(Optional.of(principal));

        when(userRepository.findByUsername("pesho"))
                .thenReturn(Optional.of(sender));

        assertFalse(userService.canAcceptRequest("pesho","Dmitri"));
    }
}
