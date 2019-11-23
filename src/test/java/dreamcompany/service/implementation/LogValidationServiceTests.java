package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.service.interfaces.LogValidationService;
import dreamcompany.service.implementation.LogValidationServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

public class LogValidationServiceTests {

    private static final String VALID_USERNAME =  "ivo";
    private static final String VALID_DESCRIPTION =  "Someone did something";
    private static final LocalDateTime VALID_CREATED_ON = LocalDateTime.now();

    private LogValidationService logValidationService;

    private LogServiceModel logServiceModel;

    @Before
    public void init(){
        logValidationService = new LogValidationServiceImpl();
        logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(VALID_USERNAME);
        logServiceModel.setDescription(VALID_DESCRIPTION);
        logServiceModel.setCreatedOn(VALID_CREATED_ON);
    }

    @Test
    public void isValid_shouldReturnTrue_withValidModel(){
        //Act
        boolean actual = logValidationService.isValid(logServiceModel);
        //Assert
        assertTrue(actual);
    }

    @Test
    public void isValid_shouldReturnFalse_withInvalidModel(){

        //Arrange
        logServiceModel.setUsername(null);
        logServiceModel.setCreatedOn(null);
        logServiceModel.setDescription(null);

        //Act
        boolean actual = logValidationService.isValid(logServiceModel);
        //Assert
        assertFalse(actual);
    }

    @Test
    public void isValid_shouldReturnFalse_withNullUsername(){
        //Arrange
        logServiceModel.setUsername(null);

        //Act
        boolean actual = logValidationService.isValid(logServiceModel);
        //Assert
        assertFalse(actual);
    }

    @Test
    public void isValid_shouldReturnFalse_withNullDescription(){
        //Arrange
        logServiceModel.setDescription(null);

        //Act
        boolean actual = logValidationService.isValid(logServiceModel);
        //Assert
        assertFalse(actual);
    }

    @Test
    public void isValid_shouldReturnFalse_withNullCreatedOn(){
        //Arrange
        logServiceModel.setCreatedOn(null);

        //Act
        boolean actual = logValidationService.isValid(logServiceModel);
        //Assert
        assertFalse(actual);
    }
}
