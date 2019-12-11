package dreamcompany.service.implementation.user;

import dreamcompany.common.GlobalConstants;
import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Role;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.domain.model.service.RoleServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.error.WrongOldPasswordException;
import dreamcompany.error.duplicates.EmailAlreadyExistException;
import dreamcompany.error.duplicates.UsernameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidUserServiceModelException;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.implementation.UserServiceImpl;
import dreamcompany.service.implementation.validation.UserValidationServiceImpl;
import dreamcompany.service.interfaces.ChatMessageService;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.LogService;
import dreamcompany.service.interfaces.RoleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.MockitoJUnitRunner;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceRegisterAndEditTests {

    private static final UserServiceModel MODEL = new UserServiceModel();
    private static final User USER_IN_DB = new User();

    private static final String VALID_USERNAME = "ivo";
    private static final String VALID_PASSWORD = "123";
    private static final String VALID_EDITED_PASSWORD = "1234";
    private static final String VALID_FIRST_NAME = "Ивайло";
    private static final String VALID_EDITED_FIRST_NAME = "Иван";
    private static final String VALID_LAST_NAME = "Николов";
    private static final String VALID_EDITED_LAST_NAME = "Иванович";
    private static final String VALID_EMAIL = "ivailo.8.1993@abv.bg";
    private static final String VALID_EDITED_EMAIL = "ivanovic.7.1983@abv.bg";

    private static final String NEW_IMAGE_URL = "[random_url]";
    private static final String NEW_IMAGE_ID = "[random_id]";

    private static final String WRONG_OLD_PASSWORD= "[wrong_old_password]";

    private static final String EXPECTED_REGISTER_LOG_MESSAGE = "Registered successfully ivo with generated id:null";
    private static final String EXPECTED_EDITED_ALL_FIELDS_EXCEPT_IMAGE_MESSAGE = "Updated successfully profile:"
            + System.lineSeparator() + "Updated email" +
            System.lineSeparator() + "Updated password"
            + System.lineSeparator() + "Updated first name"
            + System.lineSeparator() + "Updated last name"
            + System.lineSeparator();

    private static final String EXPECTED_EDITED_ALL_FIELDS_LOG_MESSAGE = EXPECTED_EDITED_ALL_FIELDS_EXCEPT_IMAGE_MESSAGE
            + "Updated image"
            + System.lineSeparator();

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private RoleService roleService;

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

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ChatMessageService chatMessageService;

    @Before
    public void init() throws RoleNotFoundException {

        UserValidationServiceImpl actualUserValidation = new UserValidationServiceImpl();
        ModelMapper actualMapper = new ModelMapper();
        BCryptPasswordEncoder actualEncoder = new BCryptPasswordEncoder();

        when(modelMapper.map(any(UserServiceModel.class), eq(User.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], User.class));

        when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenAnswer(invocationOnMock ->
                        actualMapper.map(invocationOnMock.getArguments()[0], UserServiceModel.class));

        when(roleService.findByAuthority(anyString()))
                .thenAnswer(invocationOnMock -> actualMapper.map(new Role((String) invocationOnMock.getArguments()[0]),
                        RoleServiceModel.class));

        // already tested the validation service so no need to test again
        when(validationService.isValid(any()))
                .thenAnswer(invocationOnMock -> actualUserValidation.isValid((UserServiceModel) invocationOnMock.getArguments()[0]));

        when(encoder.encode(any())).thenAnswer(invocationOnMock -> actualEncoder.encode((CharSequence) invocationOnMock.getArguments()[0]));
        when(encoder.matches(any(), any())).thenAnswer(invocationOnMock -> actualEncoder.matches((String) invocationOnMock.getArguments()[0], (String) invocationOnMock.getArguments()[1]));
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
        //Dirty hack
        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        userService.register(MODEL);

        //Assert
        verify(userRepository).save(any());
        assertEquals(EXPECTED_REGISTER_LOG_MESSAGE, actualMessage[0]);
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
            return savedInDb[0];
        });

        when(logService.create(any()))
                .thenAnswer(invocationOnMock ->
                        ((LogServiceModel) invocationOnMock.getArguments()[0]).getDescription());

        //Act
        userService.register(MODEL);

        // Assert
        int actualRolesCount = savedInDb[0].getAuthorities().size();
        Position actualPosition = savedInDb[0].getPosition();
        int expectedRolesCount = 4;
        int expectedCredits = GlobalConstants.MAX_CREDITS;
        int actualCredits = savedInDb[0].getCredits();

        assertEquals(expectedRolesCount, actualRolesCount);
        assertEquals(Position.PROJECT_MANAGER.name(), actualPosition.name());
        assertEquals(Position.PROJECT_MANAGER.getSalary(), actualPosition.getSalary());
        assertEquals(expectedCredits, actualCredits);
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
            return savedInDb[0];
        });

        when(logService.create(any()))
                .thenAnswer(invocationOnMock ->
                        ((LogServiceModel) invocationOnMock.getArguments()[0]).getDescription());

        //Act
        userService.register(MODEL);

        // Assert
        int actualRolesCount = savedInDb[0].getAuthorities().size();
        Position actualPosition = savedInDb[0].getPosition();
        int expectedRolesCount = 1;
        int expectedCredits = GlobalConstants.STARTING_CREDITS;
        int actualCredits = savedInDb[0].getCredits();

        assertEquals(expectedRolesCount, actualRolesCount);
        assertEquals(Position.INTERN.name(), actualPosition.name());
        assertEquals(Position.INTERN.getSalary(), actualPosition.getSalary());
        assertEquals(expectedCredits, actualCredits);
    }

    // happy case
    @Test
    public void edit_shouldEditAllFields_whenValidUserAndOldPassword_allFieldsEdited() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        when(userRepository.findByUsername(MODEL.getUsername()))
                .thenReturn(Optional.of(userInDb));

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        UserServiceModel edited = new UserServiceModel();
        edited.setUsername(VALID_USERNAME);
        edited.setPassword(VALID_EDITED_PASSWORD);
        edited.setFirstName(VALID_EDITED_FIRST_NAME);
        edited.setLastName(VALID_EDITED_LAST_NAME);
        edited.setEmail(VALID_EDITED_EMAIL);
        edited.setImageUrl(NEW_IMAGE_URL);
        edited.setImageId(NEW_IMAGE_ID);

        //Dirty hack
        final String[] actualLogMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualLogMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act

        UserServiceModel editedModel = userService.edit(edited, MODEL.getPassword());

        //Assert correct log message and correct
        assertEquals(EXPECTED_EDITED_ALL_FIELDS_LOG_MESSAGE, actualLogMessage[0]);
        assertEquals(VALID_EDITED_EMAIL, editedModel.getEmail());
        assertEquals(VALID_EDITED_FIRST_NAME, editedModel.getFirstName());
        assertEquals(VALID_EDITED_LAST_NAME, editedModel.getLastName());
        assertEquals(NEW_IMAGE_ID,editedModel.getImageId());
        assertEquals(NEW_IMAGE_URL, editedModel.getImageUrl());
        assertTrue(encoder.matches(VALID_EDITED_PASSWORD, editedModel.getPassword()));
    }

    @Test
    public void edit_shouldEditAllFields_whenValidUserAndOldPassword_allFieldsWithoutImageEdited() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        when(userRepository.findByUsername(MODEL.getUsername()))
                .thenReturn(Optional.of(userInDb));

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        UserServiceModel edited = new UserServiceModel();
        edited.setUsername(VALID_USERNAME);
        edited.setPassword(VALID_EDITED_PASSWORD);
        edited.setFirstName(VALID_EDITED_FIRST_NAME);
        edited.setLastName(VALID_EDITED_LAST_NAME);
        edited.setEmail(VALID_EDITED_EMAIL);

        //Dirty hack
        final String[] actualLogMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualLogMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });
        //Act

        UserServiceModel editedModel = userService.edit(edited, MODEL.getPassword());

        //Assert correct log message and correct
        assertEquals(EXPECTED_EDITED_ALL_FIELDS_EXCEPT_IMAGE_MESSAGE, actualLogMessage[0]);
        assertEquals(VALID_EDITED_EMAIL, editedModel.getEmail());
        assertEquals(VALID_EDITED_FIRST_NAME, editedModel.getFirstName());
        assertEquals(VALID_EDITED_LAST_NAME, editedModel.getLastName());
        assertTrue(encoder.matches(VALID_EDITED_PASSWORD, editedModel.getPassword()));
    }


    @Test
    public void edit_shouldDeletePreviousImage_whenUpdatedWithNew() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        userInDb.setImageId(NEW_IMAGE_ID);
        userInDb.setImageUrl(NEW_IMAGE_URL);

        when(userRepository.findByUsername(MODEL.getUsername()))
                .thenReturn(Optional.of(userInDb));

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    return logServiceModel.getDescription();
                });

        UserServiceModel edited = new UserServiceModel();
        edited.setUsername(VALID_USERNAME);
        edited.setPassword(VALID_EDITED_PASSWORD);
        edited.setFirstName(VALID_EDITED_FIRST_NAME);
        edited.setLastName(VALID_EDITED_LAST_NAME);
        edited.setEmail(VALID_EDITED_EMAIL);
        edited.setImageUrl(NEW_IMAGE_URL);
        edited.setImageId(NEW_IMAGE_ID);

        //Act
        userService.edit(edited, MODEL.getPassword());

        //Assert
        verify(cloudinaryService).deleteImage(any());
    }

    @Test(expected = InvalidUserServiceModelException.class)
    public void edit_shouldThrowException_whenInvalidUserServiceModel() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        UserServiceModel edited = new UserServiceModel();

        //Act
        userService.edit(edited, MODEL.getPassword());
    }

    @Test(expected = WrongOldPasswordException.class)
    public void edit_shouldThrowException_whenValidUserServiceModelAndInvalidOldPassword() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        when(userRepository.findByUsername(MODEL.getUsername()))
                .thenReturn(Optional.of(userInDb));

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        UserServiceModel edited = new UserServiceModel();
        edited.setUsername(VALID_USERNAME);
        edited.setPassword(VALID_EDITED_PASSWORD);
        edited.setFirstName(VALID_EDITED_FIRST_NAME);
        edited.setLastName(VALID_EDITED_LAST_NAME);
        edited.setEmail(VALID_EDITED_EMAIL);
        edited.setImageUrl(NEW_IMAGE_URL);
        edited.setImageId(NEW_IMAGE_ID);

        //Act
        userService.edit(edited, WRONG_OLD_PASSWORD);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void edit_shouldThrowException_whenTryingToEditNonExistingUser() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        when(userRepository.findByUsername(MODEL.getUsername()))
                .thenReturn(Optional.empty());

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        UserServiceModel edited = new UserServiceModel();
        edited.setUsername(VALID_USERNAME);
        edited.setPassword(VALID_EDITED_PASSWORD);
        edited.setFirstName(VALID_EDITED_FIRST_NAME);
        edited.setLastName(VALID_EDITED_LAST_NAME);
        edited.setEmail(VALID_EDITED_EMAIL);
        edited.setImageUrl(NEW_IMAGE_URL);
        edited.setImageId(NEW_IMAGE_ID);

        //Act
        userService.edit(edited, MODEL.getPassword());
    }

    @Test(expected = EmailAlreadyExistException.class)
    public void edit_shouldThrowException_whenUpdatedEmailIsTaken() throws IOException {

        //Arrange
        User userInDb = modelMapper.map(MODEL, User.class);

        when(userRepository.findByUsername(MODEL.getUsername()))
                .thenReturn(Optional.of(userInDb));

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(userInDb));

        userInDb.setPassword(encoder.encode(userInDb.getPassword()));

        UserServiceModel edited = new UserServiceModel();
        edited.setUsername(VALID_USERNAME);
        edited.setPassword(VALID_EDITED_PASSWORD);
        edited.setFirstName(VALID_EDITED_FIRST_NAME);
        edited.setLastName(VALID_EDITED_LAST_NAME);
        edited.setEmail(VALID_EDITED_EMAIL);
        edited.setImageUrl(NEW_IMAGE_URL);
        edited.setImageId(NEW_IMAGE_ID);

        //Act
        userService.edit(edited, MODEL.getPassword());
    }
}
