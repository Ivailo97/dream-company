package dreamcompany.service.implementation.validation;

import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.service.implementation.validation.ProjectValidationServiceImpl;
import dreamcompany.service.interfaces.validation.ProjectValidationService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import static org.junit.Assert.*;

public class ProjectValidationServiceTests {

    private ProjectServiceModel project;

    private ProjectValidationService validationService;

    @Before
    public void init(){
        validationService = new ProjectValidationServiceImpl();
        project = new ProjectServiceModel();
        //valid data
        project.setName("Casebook");
        project.setDescription("Social network");
        project.setReward(new BigDecimal("1500"));
    }

    @Test
    public void isValid_whenNull_shouldReturnFalse(){
        assertFalse(validationService.isValid(null));
    }

    @Test
    public void isValid_whenFieldsAreNull_shouldReturnFalse(){
        project = new ProjectServiceModel();
        assertFalse(validationService.isValid(project));
    }

    @Test
    public void isValid_whenTooShortName_shouldReturnFalse(){
        project.setName("Cas");
        assertFalse(validationService.isValid(project));
    }

    @Test
    public void isValid_whenLongName_shouldReturnFalse(){
        project.setName("Cassssssssssssssssssssssssssssssssss");
        assertFalse(validationService.isValid(project));
    }

    @Test
    public void isValid_whenNameStartsWithLowercaseLetter_shouldReturnFalse(){
        project.setName("casebook");
        assertFalse(validationService.isValid(project));
    }

    @Test
    public void isValid_whenTooShortDescription_shouldReturnFalse(){
        project.setDescription("some");
        assertFalse(validationService.isValid(project));
    }

    @Test
    public void isValid_whenNegativeReward_shouldReturnFalse(){
        project.setReward(new BigDecimal("-1000"));
        assertFalse(validationService.isValid(project));
    }

    @Test
    public void isValid_whenValid_shouldReturnTrue(){
        assertTrue(validationService.isValid(project));
    }
}
