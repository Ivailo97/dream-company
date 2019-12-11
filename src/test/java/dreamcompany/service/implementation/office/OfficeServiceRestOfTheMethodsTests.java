package dreamcompany.service.implementation.office;

import dreamcompany.domain.entity.Office;
import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.entity.Team;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.error.notexist.OfficeNotFoundException;
import dreamcompany.error.notexist.TaskNotFoundException;
import dreamcompany.repository.OfficeRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.service.interfaces.TeamService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OfficeServiceRestOfTheMethodsTests extends TestBase {

    @Autowired
    OfficeService officeService;

    @MockBean
    TeamService teamService;

    @MockBean
    OfficeRepository officeRepository;

    @Test
    public void delete_shouldDeleteOfficeWithAllTheTeamsInside_whenValidId() throws IOException {

        Office office = new Office();
        office.setAddress("ulica Vasil Levski 63A");

        Set<Team> teams = new HashSet<>();

        Team team1 = new Team();
        Team team2 = new Team();
        teams.add(team1);
        teams.add(team2);

        office.setTeams(teams);

        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(office));

        when(teamService.delete(any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        OfficeServiceModel deletedOffice = officeService.delete("officeId", "moderatorUsername");

        //Assert
        verify(teamService,times(2)).delete(any(),any());
        verify(officeRepository).delete(any());
        assertEquals(office.getAddress(),deletedOffice.getAddress());
    }

    @Test(expected = OfficeNotFoundException.class)
    public void delete_shouldThrowException_whenInvalidOfficeId() throws IOException {

        when(officeRepository.findById(any()))
                .thenReturn(Optional.empty());

        officeService.delete("officeId", "moderatorUsername");
    }

    @Test
    public void findAll_shouldReturnAllOfficesInDb_whenAny(){

        List<Office> officesInDb = List.of(new Office(), new Office());

        when(officeRepository.findAll())
                .thenReturn(officesInDb);

        List<OfficeServiceModel> returnedOffices = officeService.findAll();

        assertEquals(officesInDb.size(),returnedOffices.size());
    }

    @Test
    public void findById_shouldReturnCorrect_whenValidOfficeId() {

        Office officeInDb = new Office();
        officeInDb.setAddress("address");

        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(officeInDb));

        //Act
        OfficeServiceModel foundAndReturned = officeService.findById("officeId");

        //Assert
        assertEquals(officeInDb.getAddress(), foundAndReturned.getAddress());
    }

    @Test(expected = OfficeNotFoundException.class)
    public void findById_shouldThrowException_whenInvalidOfficeId() {

        when(officeRepository.findById(any()))
                .thenReturn(Optional.empty());

        officeService.findById("taskId");
    }
}
