package dreamcompany.validation;

import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.validation.user.service.UserValidationServiceImpl;
import dreamcompany.validation.user.service.UserValidationService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserValidationServiceTests {

    //valid data constants
    private static final String VALID_USERNAME = "ivo";
    private static final String VALID_PASSWORD = "123";
    private static final String VALID_FIRST_NAME = "Ивайло";
    private static final String VALID_LAST_NAME = "Николов";
    private static final String VALID_EMAIL = "ivailo.8.1993@abv.bg";

    //invalid data constants
    private static final String TOO_SHORT_USERNAME = "iv";
    private static final String TOO_LONG_USERNAME = "donkeyIsVeryPowerful";
    private static final String INVALID_PASSWORD = "";
    private static final String TOO_SHORT_FIRST_NAME = "le";
    private static final String TOO_LONG_FIRST_NAME = "Alexandropolis";
    private static final String TOO_SHORT_LAST_NAME = "rofl";
    private static final String TOO_LONG_LAST_NAME = "Kehlibarkovov";

    //base wrong email cases
    private static final String STARTING_WITH_SYMBOL_EMAIL = ".ivailo.8.1993@abv.bg";
    private static final String ENDING_WITH_SYMBOL_EMAIL = "ivailo.8.1993@abv.bg.";
    private static final String WITHOUT_DOMAIN_EMAIL = "ivailo.8.1993@abv";
    private static final String WITHOUT_AT_EMAIL = "ivailo.8.1993abv";


    private UserValidationService userValidationService;

    private UserServiceModel userServiceModel;

    @Before()
    public void init() {
        userValidationService = new UserValidationServiceImpl();
        userServiceModel = new UserServiceModel();
        userServiceModel.setUsername(VALID_USERNAME);
        userServiceModel.setPassword(VALID_PASSWORD);
        userServiceModel.setFirstName(VALID_FIRST_NAME);
        userServiceModel.setLastName(VALID_LAST_NAME);
        userServiceModel.setEmail(VALID_EMAIL);
    }

    @Test
    public void isValid_whenNull_false() {
        assertFalse(userValidationService.isValid(null));
    }

    @Test
    public void isValid_whenAllFieldsNull_false() {
        //arrange
        userServiceModel = new UserServiceModel();
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenTooShortUsername_false() {
        //arrange
        userServiceModel.setUsername(TOO_SHORT_USERNAME);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenTooLongUsername_false() {
        //arrange
        userServiceModel.setUsername(TOO_LONG_USERNAME);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }


    @Test
    public void isValid_whenTooShortFirstName_false() {
        //arrange
        userServiceModel.setFirstName(TOO_SHORT_FIRST_NAME);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenTooLongLastName_false() {
        //arrange
        userServiceModel.setLastName(TOO_LONG_LAST_NAME);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenTooShortLastName_false() {
        //arrange
        userServiceModel.setLastName(TOO_SHORT_LAST_NAME);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenTooLongFirstName_false() {
        //arrange
        userServiceModel.setFirstName(TOO_LONG_FIRST_NAME);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }


    @Test
    public void isValid_whenInvalidPassword_false() {
        //arrange
        userServiceModel.setPassword(INVALID_PASSWORD);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenEmailStartsWithSymbol_false(){
        //arrange
        userServiceModel.setEmail(STARTING_WITH_SYMBOL_EMAIL);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenEmailEndsWithSymbol_false(){
        //arrange
        userServiceModel.setEmail(ENDING_WITH_SYMBOL_EMAIL);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenEmailHasNoDomain_false(){
        //arrange
        userServiceModel.setEmail(WITHOUT_DOMAIN_EMAIL);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenEmailHasNoAt_false(){
        //arrange
        userServiceModel.setEmail(WITHOUT_AT_EMAIL);
        //act and assert
        assertFalse(userValidationService.isValid(userServiceModel));
    }

    @Test
    public void isValid_whenValid_true() {
        assertTrue(userValidationService.isValid(userServiceModel));
    }
}
