package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Role;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.domain.model.service.RoleServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.error.duplicates.EmailAlreadyExistException;
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
    public void init() throws RoleNotFoundException {

        UserValidationServiceImpl actualUserValidation = new UserValidationServiceImpl();
        ModelMapper actualMapper = new ModelMapper();

        when(modelMapper.map(any(UserServiceModel.class),eq(User.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], User.class));

        when(roleService.findByAuthority(anyString()))
                .thenAnswer(invocationOnMock -> actualMapper.map(new Role((String) invocationOnMock.getArguments()[0]),
                        RoleServiceModel.class));

        // already tested the validation service so no need to test again
        when(validationService.isValid(any()))
                .thenAnswer(invocationOnMock -> actualUserValidation.isValid((UserServiceModel) invocationOnMock.getArguments()[0]));

        when(encoder.encode(any())).thenReturn(RANDOM_HASHED_PASSWORD);
        when(userRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(logService.create(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        USER_IN_DB.setUsername(VALID_USERNAME);
        USER_IN_DB.setEmail(VALID_EMAIL);
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
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> ((LogServiceModel)invocationOnMock.getArguments()[0]).getDescription());

        //Act
        String actual = userService.register(MODEL);

        //Assert
        verify(userRepository).save(any());
        assertEquals(EXPECTED_REGISTER_LOG_MESSAGE,actual);
    }

    @Test(expected = UsernameAlreadyExistException.class)
    public void register_shouldThrowException_whenDuplicateUserUsername() throws RoleNotFoundException {

        //Arrange
        when(userRepository.findByUsername(MODEL.getUsername())).thenReturn(Optional.of(USER_IN_DB));

        //Act
        userService.register(MODEL);
    }

    @Test(expected = EmailAlreadyExistException.class)
    public void register_shouldThrowException_whenDuplicateUserEmail() throws RoleNotFoundException {

        //Arrange
        when(userRepository.findByEmail(MODEL.getEmail())).thenReturn(Optional.of(USER_IN_DB));

        //Act
        userService.register(MODEL);
    }

    @Test
    public void register_shouldDefineAllRolesProjectManagerPositionAndMaxCredits_whenFirstUser() throws RoleNotFoundException {

        //Arrange
        when(userRepository.count()).thenReturn(0L);

        //dirty hack ;(
        final User[] savedInDb = new User[1];

        // makes sure when save method is called to return the entity which is passed as an argument
        when(userRepository.save(any())).thenAnswer(invocationOnMock -> {
            savedInDb[0] = (User) invocationOnMock.getArguments()[0];
            return  savedInDb[0];
        });

        when(logService.create(any()))
                .thenAnswer(invocationOnMock ->
                        ((LogServiceModel)invocationOnMock.getArguments()[0]).getDescription());

        //Act
        userService.register(MODEL);

        // Assert
        int actualRolesCount = savedInDb[0].getAuthorities().size();
        Position actualPosition = savedInDb[0].getPosition();
        int expectedRolesCount = 4;
        int expectedCredits = GlobalConstraints.MAX_CREDITS;
        int actualCredits = savedInDb[0].getCredits();

        assertEquals(expectedRolesCount,actualRolesCount);
        assertEquals(Position.PROJECT_MANAGER.name(),actualPosition.name());
        assertEquals(Position.PROJECT_MANAGER.getSalary(),actualPosition.getSalary());
        assertEquals(expectedCredits,actualCredits);
    }

    @Test
    public void register_shouldDefineUserRoleInternPositionAndStartingCredits_whenNotFirstUser() throws RoleNotFoundException {

        //Arrange
        when(userRepository.count()).thenReturn(1L);

        //dirty hack ;(
        final User[] savedInDb = new User[1];

        // makes sure when save method is called to return the entity which is passed as an argument
        when(userRepository.save(any())).thenAnswer(invocationOnMock -> {
            savedInDb[0] = (User) invocationOnMock.getArguments()[0];
            return  savedInDb[0];
        });

        when(logService.create(any()))
                .thenAnswer(invocationOnMock ->
                        ((LogServiceModel)invocationOnMock.getArguments()[0]).getDescription());

        //Act
        userService.register(MODEL);

        // Assert
        int actualRolesCount = savedInDb[0].getAuthorities().size();
        Position actualPosition = savedInDb[0].getPosition();
        int expectedRolesCount = 1;
        int expectedCredits = GlobalConstraints.STARTING_CREDITS;
        int actualCredits = savedInDb[0].getCredits();

        assertEquals(expectedRolesCount,actualRolesCount);
        assertEquals(Position.INTERN.name(),actualPosition.name());
        assertEquals(Position.INTERN.getSalary(),actualPosition.getSalary());
        assertEquals(expectedCredits,actualCredits);
    }
}
