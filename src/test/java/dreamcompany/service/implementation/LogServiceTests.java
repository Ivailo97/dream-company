package dreamcompany.service.implementation;

import dreamcompany.domain.entity.Log;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.LogRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.implementation.validation.LogValidationServiceImpl;
import dreamcompany.service.interfaces.validation.LogValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class LogServiceTests {

    private final User USER_IN_DB = new User();

    private final LogServiceModel LOG_SERVICE_MODEL = new LogServiceModel();
    private static final String VALID_USERNAME = "ivo";
    private static final String VALID_DESCRIPTION = "Someone did something";
    private static final LocalDateTime VALID_CREATED_ON = LocalDateTime.now();

    @InjectMocks
    private LogServiceImpl logService;

    @Mock
    private LogRepository logRepository;

    @Mock
    private LogValidationService logValidationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void init() {
        LogValidationService actualValidationService = new LogValidationServiceImpl();
        ModelMapper actualMapper = new ModelMapper();

        USER_IN_DB.setUsername(VALID_USERNAME);
        LOG_SERVICE_MODEL.setUsername(USER_IN_DB.getUsername());
        LOG_SERVICE_MODEL.setDescription(VALID_DESCRIPTION);
        LOG_SERVICE_MODEL.setCreatedOn(VALID_CREATED_ON);


        // maps service model to entity correct
        when(modelMapper.map(LOG_SERVICE_MODEL, Log.class))
                .thenReturn(actualMapper.map(LOG_SERVICE_MODEL, Log.class));

        when(logValidationService.isValid(any()))
                .then(invocationOnMock -> actualValidationService.isValid((LogServiceModel) invocationOnMock.getArguments()[0]));
    }

    @Test
    public void create_shouldCreate_withValidModel() {

        // Arrange

        // when the user exist in db
        when(userRepository.findByUsername(USER_IN_DB.getUsername()))
                .thenReturn(Optional.of(USER_IN_DB));

        // repository save method returns the entity instead of saving in db
        when(logRepository.save(any()))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        // Act
        String returned = logService.create(LOG_SERVICE_MODEL);

        // Assert
        verify(logRepository).save(any());
        assertEquals(returned,LOG_SERVICE_MODEL.getDescription());
    }

    @Test(expected = UserNotFoundException.class)
    public void create_shouldThrowException_whenLogUsernameNotBelongToAUserInDatabase() {

        // Arrange
        // when the user doesnt exist in db
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.empty());

        // Act
        logService.create(LOG_SERVICE_MODEL);
    }

    // illegal argument exception for now may change later

    @Test(expected = IllegalArgumentException.class)
    public void create_shouldThrowException_whenCreatedOnIsNull(){

        // Arrange
        LOG_SERVICE_MODEL.setCreatedOn(null);

        // Act
        logService.create(LOG_SERVICE_MODEL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_shouldThrowException_whenDescriptionIsNull(){

        // Arrange
        LOG_SERVICE_MODEL.setDescription(null);

        // Act
        logService.create(LOG_SERVICE_MODEL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_shouldThrowException_whenUsernameIsNull(){

        // Arrange
        LOG_SERVICE_MODEL.setUsername(null);

        // Act
        logService.create(LOG_SERVICE_MODEL);
    }
}
