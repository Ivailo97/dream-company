package dreamcompany.service.implementation;

import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.error.duplicates.UsernameAlreadyExistException;
import dreamcompany.repository.RoleRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.LogService;
import dreamcompany.service.interfaces.RoleService;
import dreamcompany.validation.service.UserValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;


import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTests {

    private static final UserServiceModel MODEL = new UserServiceModel();
    private static final User USER_IN_DB = new User();

    private static final String VALID_USERNAME = "ivo";
    private static final String VALID_PASSWORD = "123";
    private static final String VALID_FIRST_NAME = "Ивайло";
    private static final String VALID_LAST_NAME = "Николов";
    private static final String VALID_EMAIL = "ivailo.8.1993@abv.bg";
    private static final String RANDOM_HASHED_PASSWORD = "#$Ssda#@!";
    private static final String EXPECTED_REGISTER_LOG_MESSAGE = "Registered successfully ivo with generated id:null";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserValidationServiceImpl validationService;

    @Mock
    private LogService logService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void init() {
        USER_IN_DB.setUsername(VALID_USERNAME);
        when(encoder.encode(any())).thenReturn(RANDOM_HASHED_PASSWORD);
        when(userRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(logService.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        MODEL.setUsername(VALID_USERNAME);
        MODEL.setPassword(VALID_PASSWORD);
        MODEL.setFirstName(VALID_FIRST_NAME);
        MODEL.setLastName(VALID_LAST_NAME);
        MODEL.setEmail(VALID_EMAIL);
    }

    // happy case
    @Test
    public void register_shouldAddUserInDb_andReturnCorrectLogDescription_whenValidUser() throws RoleNotFoundException {

        //Arrange
        User entity = new ModelMapper().map(MODEL, User.class);
        when(modelMapper.map(MODEL, User.class)).thenReturn(entity);
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> ((LogServiceModel)invocationOnMock.getArguments()[0]).getDescription());

        // we tested this already and know it works
        when(validationService.isValid(any())).thenReturn(true);

        //Act
        String actual = userService.register(MODEL);

        //Assert
        verify(userRepository).save(any());
        assertEquals(EXPECTED_REGISTER_LOG_MESSAGE,actual);
    }

    @Test(expected = UsernameAlreadyExistException.class)
    public void register_shouldThrowException_whenInvalidUser() throws RoleNotFoundException {

        //Arrange
        // returns user in db a.k.a duplicate username
        when(userRepository.findByUsername(MODEL.getUsername())).thenReturn(Optional.of(USER_IN_DB));

        //Act
        userService.register(MODEL);
    }
}
