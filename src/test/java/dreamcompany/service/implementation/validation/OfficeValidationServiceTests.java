package dreamcompany.service.implementation.validation;

import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.service.implementation.validation.OfficeValidationServiceImpl;
import dreamcompany.service.interfaces.validation.OfficeValidationService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OfficeValidationServiceTests {

    //valid data
    private static final String VALID_ADDRESS = "ulica Vasil Levski 63A blok 31 vhod B";
    private static final String VALID_PHONE_NUMBER = "0892945039";
    private static final String VALID_COUNTRY = "Bulgaria";
    private static final String VALID_TOWN = "Sofia";

    //invalid data
    private static final String TOO_SHORT_ADDRESS = "ulic";
    private static final String TOO_LONG_ADDRESS = "ulica Vasil Levski 63A blok 31 vhod B etaj 10 vtora krachka v lqvo do televizora";
    private static final String TOO_LONG_PHONE_NUMBER = "08929450391111";
    private static final String TOO_SHORT_PHONE_NUMBER = "089294";
    private static final String COUNTRY_STARTING_WITH_LOWERCASE_LETTER = "bulgaria";
    private static final String TOWN_STARTING_WITH_LOWERCASE_LETTER = "sofia";

    private OfficeServiceModel officeServiceModel;

    private OfficeValidationService officeValidationService;

    @Before
    public void init(){
        officeServiceModel = new OfficeServiceModel();
        officeServiceModel.setAddress(VALID_ADDRESS);
        officeServiceModel.setCountry(VALID_COUNTRY);
        officeServiceModel.setTown(VALID_TOWN);
        officeServiceModel.setPhoneNumber(VALID_PHONE_NUMBER);
        officeValidationService = new OfficeValidationServiceImpl();
    }

    @Test
    public void isValid_whenNull_shouldReturnFalse(){
        assertFalse(officeValidationService.isValid(null));
    }

    @Test
    public void isValid_whenAllFieldsAreNull_shouldReturnFalse(){
        officeServiceModel = new OfficeServiceModel();
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenTooShortAddress_shouldReturnFalse(){
        officeServiceModel.setAddress(TOO_SHORT_ADDRESS);
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenTooLongAddress_shouldReturnFalse(){
        officeServiceModel.setAddress(TOO_LONG_ADDRESS);
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenTooShortPhoneNumber_shouldReturnFalse(){
        officeServiceModel.setPhoneNumber(TOO_SHORT_PHONE_NUMBER);
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenTooLongPhoneNumber_shouldReturnFalse(){
        officeServiceModel.setPhoneNumber(TOO_LONG_PHONE_NUMBER);
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenCountryStartsWithLowercaseLetter_shouldReturnFalse(){
        officeServiceModel.setCountry(COUNTRY_STARTING_WITH_LOWERCASE_LETTER);
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenTownStartsWithLowercaseLetter_shouldReturnFalse(){
        officeServiceModel.setTown(TOWN_STARTING_WITH_LOWERCASE_LETTER);
        assertFalse(officeValidationService.isValid(officeServiceModel));
    }

    @Test
    public void isValid_whenValid_shouldReturnTrue(){
        assertTrue(officeValidationService.isValid(officeServiceModel));
    }
}
