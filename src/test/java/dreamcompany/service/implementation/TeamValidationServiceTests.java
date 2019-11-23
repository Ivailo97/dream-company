package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.service.implementation.TeamValidationServiceImpl;
import dreamcompany.service.interfaces.TeamValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamValidationServiceTests {

    private TeamServiceModel teamServiceModel;

    private TeamValidationService teamValidationService;

    @Before
    public void init() {
        teamValidationService = new TeamValidationServiceImpl();
        // valid service model
        teamServiceModel = new TeamServiceModel();
        teamServiceModel.setName("Softuni");
        teamServiceModel.setLogoId("aksodkasjd");
        teamServiceModel.setLogoUrl("asdasdasdpl");

        OfficeServiceModel officeServiceModel = new OfficeServiceModel();

        Set<UserServiceModel> employeesInTeam = new HashSet<>();
        employeesInTeam.add(new UserServiceModel());
        employeesInTeam.add(new UserServiceModel());

        teamServiceModel.setOffice(officeServiceModel);
        teamServiceModel.setEmployees(employeesInTeam);
    }

    @Test
    public void isValid_whenNull_shouldReturnFalse() {
        assertFalse(teamValidationService.isValid(null));
    }

    @Test
    public void isValid_whenAllFieldsAreNull_shouldReturnFalse() {
       teamServiceModel = new TeamServiceModel();
        assertFalse(teamValidationService.isValid(teamServiceModel));
    }

    @Test
    public void isValid_whenTooShortName_shouldReturnFalse() {
        teamServiceModel.setName("Lol");
        assertFalse(teamValidationService.isValid(teamServiceModel));
    }

    @Test
    public void isValid_whenTooLongName_shouldReturnFalse() {
        teamServiceModel.setName("Prekalenodulgoime");
        assertFalse(teamValidationService.isValid(teamServiceModel));
    }

    @Test
    public void isValid_whenNameStartsWithLowercaseLetter_shouldReturnFalse() {
        teamServiceModel.setName("rocket");
        assertFalse(teamValidationService.isValid(teamServiceModel));
    }

    @Test
    public void isValid_whenEmployeesAreLessThanMinimum_shouldReturnFalse(){
        teamServiceModel.setEmployees(new HashSet<>());
        assertFalse(teamValidationService.isValid(teamServiceModel));
    }

    @Test
    public void isValid_whenValid_shouldReturnTrue() {
        assertTrue(teamValidationService.isValid(teamServiceModel));
    }
}
