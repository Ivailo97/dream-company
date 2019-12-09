package dreamcompany.service.implementation;


import dreamcompany.domain.entity.User;
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

    @Test
    public void canRemoveFriend_shouldReturnTrue_whenLoggedUserHasThatFriendInList() {

        String loggedUserUsername = "Dmitri";
        String friendUsername = "Spas";

        User principal = new User();
        principal.setUsername(loggedUserUsername);
        principal.setFriends(new HashSet<>());

        User friend = new User();
        friend.setUsername(friendUsername);

        principal.getFriends().add(friend);

        when(userRepository.findByUsername(loggedUserUsername))
                .thenReturn(Optional.of(principal));

        //Act and assert
        assertTrue(userService.canRemoveFriend(loggedUserUsername, friendUsername));
    }

    @Test
    public void canRemoveFriend_shouldReturnFalse_whenLoggedUserDoesntHaveThatFriendInList() {

        String loggedUserUsername = "Dmitri";
        String friendUsername = "Vlad";

        User principal = new User();
        principal.setUsername(loggedUserUsername);
        principal.setFriends(new HashSet<>());

        User friend = new User();
        friend.setUsername("Vladii");

        principal.getFriends().add(friend);

        when(userRepository.findByUsername(loggedUserUsername))
                .thenReturn(Optional.of(principal));

        //Act and assert
        assertFalse(userService.canRemoveFriend(loggedUserUsername, friendUsername));
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

        String loggedUserUsername = "Dmitri";
        String friendUsername = "Vlad";

        when(userRepository.findByUsername(loggedUserUsername))
                .thenReturn(Optional.empty());

        //Act
        userService.canRemoveFriend(loggedUserUsername, friendUsername);
    }


}
