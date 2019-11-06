package dreamcompany.service.implementation;

import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceRestOfTheMethodsTests {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void init(){

        ModelMapper actualMapper = new ModelMapper();




    }


    @Test
    public void findByUsername_shouldFindCorrect_whenUsernameExist(){


    }
}
