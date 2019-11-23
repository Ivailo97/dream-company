package dreamcompany.service.implementation;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.service.interfaces.TaskValidationService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TaskValidationServiceTests {

    private TaskValidationService service;

    private TaskServiceModel task;

    @Before
    public void init(){
        service = new TaskValidationServiceImpl();
        task = new TaskServiceModel();
        //valid data
        task.setProject("someString");
        task.setCredits(3);
        task.setName("Code");
        task.setRequiredPosition(Position.JUNIOR);
        task.setDescription("Do some java mate");
        task.setMinutesNeeded(1);
    }

    @Test
    public void isValid_whenNull_shouldReturnFalse(){
        assertFalse(service.isValid(null));
    }

    @Test
    public void isValid_whenFieldsAreNull_shouldReturnFalse(){
        task = new TaskServiceModel();
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenNameTooShort_shouldReturnFalse(){
        task.setName("Tok");
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenNameTooLong_shouldReturnFalse(){
        task.setName("Reallylongtaskname");
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenNameStartWithLowercaseLetter_shouldReturnFalse(){
        task.setName("some");
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenDescriptionTooShort_shouldReturnFalse(){
        task.setDescription("lol");
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenCreditsAreLessThanMinimum_shouldReturnFalse(){
        task.setCredits(1);
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenCreditsAreMoreThanMax_shouldReturnFalse(){
        task.setCredits(21);
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenMinutesAreLessThanMinimum_shouldReturnFalse(){
        task.setMinutesNeeded(0);
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenMinutesAreMoreThanMaximum_shouldReturnFalse(){
        task.setMinutesNeeded(61);
        assertFalse(service.isValid(task));
    }

    @Test
    public void isValid_whenValid_shouldReturnTrue(){
        assertTrue(service.isValid(task));
    }
}
