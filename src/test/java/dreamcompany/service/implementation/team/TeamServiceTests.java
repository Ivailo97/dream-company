package dreamcompany.service.implementation.team;

import dreamcompany.domain.entity.*;
import dreamcompany.domain.model.service.*;
import dreamcompany.error.duplicates.TeamNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidTeamServiceModelException;
import dreamcompany.error.notexist.OfficeNotFoundException;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.error.notexist.TeamNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.*;
import dreamcompany.service.implementation.LogServiceImpl;
import dreamcompany.service.implementation.TeamServiceImpl;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.validation.TeamValidationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TeamServiceTests {

    final String CREATED_SUCCESSFULLY = "Created team with following name: Softuni";
    final String FIRST_EMPLOYEE_FULL_NAME = "Минка Вазова";
    final String SECOND_EMPLOYEE_FULL_NAME = "Минчо Минчев";

    final String UPDATE_TEAM_SUCCESSFULLY = "Updated team: Softuni successfully:";
    final String UPDATED_TEAM_NAME = "Updated team name to: Softuni2";
    final String UPDATED_TEAM_OFFICE = "Updated office";
    final String UPDATED_TEAM_LOGO = "Updated team logo";

    final String ASSIGNED_PROJECT_SUCCESSFULLY = "Assigned project with name: XXX to team with name: YYY successfully";
    final String ADDED_EMPLOYEE_TO_TEAM_SUCCESSFULLY = "Added employee with name: Margarin Lee to team with name: CSKA successfully";
    final String REMOVED_EMPLOYEE_FROM_TEAM_SUCCESSFULLY = "Removed employee with name: John Doe from team with name: CSKA successfully";


    @InjectMocks
    TeamServiceImpl service;

    @Mock
    UserRepository userRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    OfficeRepository officeRepository;

    @Mock
    TeamValidationService validationService;

    @Mock
    CloudinaryService cloudinaryService;

    @Mock
    RoleRepository roleRepository;

    @Mock
    TaskRepository taskRepository;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    LogServiceImpl logService;

    @Mock
    ModelMapper mapper;

    TeamServiceModel model;

    @Before
    public void init() {
        model = new TeamServiceModel();

        ModelMapper actualMapper = new ModelMapper();

        when(roleRepository.findByAuthority(any()))
                .thenAnswer(invocationOnMock ->
                        Optional.of(new Role((String) invocationOnMock.getArguments()[0])));

        when(mapper.map(any(TeamServiceModel.class), eq(Team.class)))
                .thenAnswer(invocation ->
                        actualMapper.map(invocation.getArguments()[0], Team.class));

        when(mapper.map(any(Team.class), eq(TeamServiceModel.class)))
                .thenAnswer(invocation ->
                        actualMapper.map(invocation.getArguments()[0], TeamServiceModel.class));

        when(logService.create(any()))
                .thenReturn(null);
    }

    @Test
    public void create_shouldSaveTeamInDbMakeTheFirstEmployeeTeamLeadAndReturnCorrectLogDescription_whenValidModel() {

        // Arrange -> setting up the model with valid data
        model.setName("Softuni");

        OfficeServiceModel officeModel = new OfficeServiceModel();
        officeModel.setId("randomIdasas");
        model.setOffice(officeModel);

        Set<UserServiceModel> employeeModels = new LinkedHashSet<>();

        UserServiceModel firstEmployeeModel = new UserServiceModel();
        firstEmployeeModel.setFirstName("Минчо");
        firstEmployeeModel.setLastName("Минчев");
        employeeModels.add(firstEmployeeModel);

        UserServiceModel secondEmployeeModel = new UserServiceModel();
        secondEmployeeModel.setFirstName("Минка");
        secondEmployeeModel.setLastName("Вазова");
        employeeModels.add(secondEmployeeModel);

        model.setEmployees(employeeModels);

        //make sure validation service returns true
        when(validationService.isValid(any()))
                .thenReturn(true);

        // mocking the db logic
        Office officeInDb = new Office();
        officeInDb.setAddress("ulica Vasil Levski 63A");

        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(officeInDb));

        User firstUserInDb = new User();
        firstUserInDb.setFirstName("Минчо");
        firstUserInDb.setLastName("Минчев");
        firstUserInDb.setPosition(Position.INTERN);
        firstUserInDb.setAuthorities(new HashSet<>());

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(firstUserInDb));

        when(teamRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        TeamServiceModel actual = service.create(model, "random");

        //Assert
        verify(teamRepository).save(any());
        assertEquals(model.getName(), actual.getName());
        assertTrue(actualMessage[0].contains(CREATED_SUCCESSFULLY));
        assertTrue(actualMessage[0].contains(FIRST_EMPLOYEE_FULL_NAME));
        assertTrue(actualMessage[0].contains(SECOND_EMPLOYEE_FULL_NAME));
        assertEquals(firstUserInDb.getPosition(), Position.TEAM_LEADER);
    }

    @Test(expected = InvalidTeamServiceModelException.class)
    public void create_shouldThrowException_whenNull() {
        service.create(null, null);
    }

    @Test(expected = InvalidTeamServiceModelException.class)
    public void create_shouldThrowException_whenInvalidServiceModel() {
        service.create(model, "randomModeratorName");
    }

    @Test(expected = InvalidTeamServiceModelException.class)
    public void create_shouldThrowException_whenEmployeeCountLessThanMin() {
        model.setEmployees(new HashSet<>());
        service.create(model, "randomModeratorName");
    }


    @Test(expected = TeamNameAlreadyExistException.class)
    public void create_shouldThrowException_whenTeamWithSameNameExistAlready() {
        when(validationService.isValid(any()))
                .thenReturn(true);
        when(teamRepository.findByName(any()))
                .thenReturn(Optional.of(new Team()));
        service.create(model, "randomModeratorName");
    }

    @Test
    public void edit_shouldEditCorrectly_whenValidParams() throws IOException {
        //edited model
        model.setName("Softuni2");
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        officeServiceModel.setId("editedTeamOfficeId");
        model.setLogoUrl("randomNewLogoUrl");
        model.setOffice(officeServiceModel);

        Office selectedNewOffice = new Office();
        selectedNewOffice.setAddress("Random new address");

        when(officeRepository.findById(any()))
                .thenReturn(Optional.of(selectedNewOffice));

        // team in db with office
        Team teamInDb = new Team();
        teamInDb.setName("Softuni");
        teamInDb.setLogoUrl("randomLogoUrl");
        Office officeInDb = new Office();
        officeInDb.setId("officeInDbId");
        officeInDb.setAddress("Random address");
        teamInDb.setOffice(officeInDb);

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        when(teamRepository.save(any()))
                .thenReturn(null);

        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        service.edit("random", model, "randomModerator");

        //Assert
        assertEquals("randomNewLogoUrl", teamInDb.getLogoUrl());
        assertEquals("Softuni2", teamInDb.getName());
        assertEquals("Random new address", teamInDb.getOffice().getAddress());
        assertTrue(actualMessage[0].contains(UPDATE_TEAM_SUCCESSFULLY));
        assertTrue(actualMessage[0].contains(UPDATED_TEAM_LOGO));
        assertTrue(actualMessage[0].contains(UPDATED_TEAM_OFFICE));
        assertTrue(actualMessage[0].contains(UPDATED_TEAM_NAME));
    }

    @Test(expected = InvalidTeamServiceModelException.class)
    public void edit_shouldThrowException_whenNull() throws IOException {
        service.edit("random", model, "randomModerator");
    }

    @Test(expected = TeamNameAlreadyExistException.class)
    public void edit_shouldThrowException_whenEditedModelHasTakenName() throws IOException {

        //edited model
        model.setName("Softuni2");
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        model.setOffice(officeServiceModel);

        // team in db with office
        Team teamInDb = new Team();
        teamInDb.setName("Softuni");

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        when(teamRepository.findByName(any()))
                .thenReturn(Optional.of(new Team()));

        //Act
        service.edit("random", model, "randomModerator");
    }

    @Test(expected = TeamNotFoundException.class)
    public void edit_shouldThrowException_whenPassedTeamIdParameterNotValid() throws IOException {

        //edited model
        model.setName("Softuni2");
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        model.setOffice(officeServiceModel);

        when(teamRepository.findById(any()))
                .thenReturn(Optional.empty());

        //Act
        service.edit("random", model, "randomModerator");
    }

    //actually this is not needed...its never the case lol
    @Test(expected = OfficeNotFoundException.class)
    public void edit_shouldThrowException_whenPassedInvalidOfficeIdInModel() throws IOException {

        //edited model
        model.setName("Softuni");
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        model.setOffice(officeServiceModel);

        // team in db
        Team teamInDb = new Team();
        teamInDb.setName("Softuni");

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        when(officeRepository.findById(any()))
                .thenReturn(Optional.empty());

        //Act
        service.edit("random", model, "randomModerator");
    }


    @Test
    public void delete_shouldDeleteFromDbTeamSetEmployeesFreeAndProjectStatusToPendingIfAny_whenValidParams() throws IOException {

        //Arrange
        // team in db
        Team teamInDb = new Team();
        teamInDb.setTeamLeaderPreviousPosition(Position.INTERN);

        Set<User> employeesInTeam = new HashSet<>();
        User employee = new User();
        employee.setTeam(teamInDb);
        employeesInTeam.add(employee);

        Project assignedProject = new Project();
        assignedProject.setStatus(Status.IN_PROGRESS);

        Set<Task> projectTasks = new HashSet<>();
        Task task = new Task();
        task.setEmployee(employee);
        task.setStatus(Status.IN_PROGRESS);

        projectTasks.add(task);
        assignedProject.setTasks(projectTasks);

        teamInDb.setProject(assignedProject);
        employee.setPosition(Position.TEAM_LEADER);
        teamInDb.setEmployees(employeesInTeam);
        teamInDb.setName("Softuni");

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        when(userRepository.save(any()))
                .thenReturn(null);

        when(taskRepository.save(any()))
                .thenReturn(null);

        when(projectRepository.save(any()))
                .thenReturn(null);

        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        service.delete("validTeamId", "randomModeratorName");

        //Assert
        verify(teamRepository).delete(any());
        assertEquals(Status.PENDING, assignedProject.getStatus());
        assertEquals(Status.PENDING, task.getStatus());
        assertNull(task.getEmployee());
        assertEquals(Position.INTERN, employee.getPosition());
    }

    @Test(expected = TeamNotFoundException.class)
    public void delete_shouldThrowException_whenCalledWithInvalidParam() throws IOException {

        when(teamRepository.findById(any()))
                .thenReturn(Optional.empty());

        service.delete("invalidId", "randomModerator");
    }

    @Test
    public void findById_shouldReturnCorrect_whenValidId() {

        Team teamInDb = new Team();
        teamInDb.setName("Softuni");

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        TeamServiceModel teamServiceModel = service.findById("validId");
        assertEquals(teamInDb.getName(), teamServiceModel.getName());
    }

    @Test(expected = TeamNotFoundException.class)
    public void findById_shouldThrowException_whenInvalidId() {
        when(teamRepository.findById(any()))
                .thenReturn(Optional.empty());

        service.findById("invalidId");
    }

    @Test
    public void findAll_shouldReturnAllInDb() {

        List<Team> fakeDb = new ArrayList<>();
        Team teamInDb = new Team();
        fakeDb.add(teamInDb);

        when(teamRepository.findAll())
                .thenReturn(fakeDb);

        List<TeamServiceModel> returned = service.findAll();
        assertEquals(fakeDb.size(), returned.size());
    }

    @Test
    public void assignProject_shouldAssignProjectToATeam_whenValidParameters() {

        Project project = new Project();
        project.setName("XXX");
        Team team = new Team();
        team.setName("YYY");

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(team));

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(project));

        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        service.assignProject("validProjectID", "validTeamId", "randomManagerUsername");

        //Assert
        assertEquals(Status.IN_PROGRESS, project.getStatus());
        assertEquals(project.getName(), team.getProject().getName());
        assertEquals(ASSIGNED_PROJECT_SUCCESSFULLY, actualMessage[0]);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void assignProject_shouldThrowException_whenInvalidProjectId() {

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());
        service.assignProject("invalidProjectId", "randomId", "managerUsername");
    }

    @Test(expected = TeamNotFoundException.class)
    public void assignProject_shouldThrowException_whenInvalidTeamId() {

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(new Project()));

        when(teamRepository.findById(any()))
                .thenReturn(Optional.empty());
        service.assignProject("randomId", "invalidTeamId", "managerUsername");
    }

    @Test
    public void findAllWithoutProject_shouldReturnCorrect() {

        List<Team> teamsInDb = new ArrayList<>();
        Team team = new Team();
        teamsInDb.add(team);

        when(teamRepository.findAllByProjectNull())
                .thenReturn(teamsInDb);

        List<TeamServiceModel> returned = service.findAllWithoutProject();
        assertEquals(teamsInDb.size(), returned.size());
    }

    @Test
    public void addEmployeeToTeam_shouldAddUserToATeam_whenValidParams() {

        Team teamInDb = new Team();
        teamInDb.setName("CSKA");

        User employee = new User();
        employee.setFirstName("Margarin");
        employee.setLastName("Lee");

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        when(userRepository.findById(any()))
                .thenReturn(Optional.of(employee));

        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        service.addEmployeeToTeam("validId", "validId", "moderatorUsername");

        //Assert
        assertEquals(ADDED_EMPLOYEE_TO_TEAM_SUCCESSFULLY, actualMessage[0]);
        assertEquals(teamInDb.getName(), employee.getTeam().getName());
    }

    @Test(expected = TeamNotFoundException.class)
    public void addEmployeeToTeam_shouldThrowException_whenInvalidTeamId() {

        when(teamRepository.findById(any()))
                .thenReturn(Optional.empty());

        service.addEmployeeToTeam("invalidId", "validId", "moderatorUsername");
    }

    @Test(expected = UserNotFoundException.class)
    public void addEmployeeToTeam_shouldThrowException_whenInvalidUserId() {

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(new Team()));

        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        service.addEmployeeToTeam("invalidId", "validId", "moderatorUsername");
    }

    @Test
    public void removeEmployeeFromTeam_shouldRemoveUserFromATeamIfTeamLeaderSetPrevPosition_whenEnoughEmployeesInTeam() {

        Team teamInDb = new Team();
        teamInDb.setName("CSKA");
        teamInDb.setTeamLeaderPreviousPosition(Position.INTERN);
        Set<User> employees = new HashSet<>();
        User teamLeader = new User();
        teamLeader.setFirstName("John");
        teamLeader.setLastName("Doe");
        teamLeader.setPosition(Position.TEAM_LEADER);
        teamLeader.setSalary(Position.TEAM_LEADER.getSalary());
        teamLeader.setTeam(teamInDb);
        employees.add(teamLeader);
        User employee1 = new User();
        employee1.setTeam(teamInDb);
        employees.add(employee1);
        User employee2 = new User();
        employee2.setTeam(teamInDb);
        employees.add(employee2);
        teamInDb.setEmployees(employees);

        when(userRepository.findById(any()))
                .thenAnswer(invocation -> {
                    teamLeader.getTeam().getEmployees().remove(teamLeader);
                  return   Optional.of(teamLeader);
                });

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        when(userRepository.save(any()))
                .thenReturn(null);

        //Act
        service.removeEmployeeFromTeam("validId", "validId", "moderatorUsername");

        //Assert
        assertEquals(REMOVED_EMPLOYEE_FROM_TEAM_SUCCESSFULLY, actualMessage[0]);
        assertEquals(Position.INTERN, teamLeader.getPosition());
        assertEquals(Position.INTERN.getSalary(), teamLeader.getSalary());
    }

    @Test(expected = TeamNotFoundException.class)
    public void removeEmployeeFromTeam_shouldThrowException_whenInvalidTeamId(){

        when(teamRepository.findById(any()))
                .thenReturn(Optional.empty());

        service.removeEmployeeFromTeam("id","id","moderatorName");
    }

    @Test(expected = UserNotFoundException.class)
    public void removeEmployeeFromTeam_shouldThrowException_whenInvalidUserId(){

        Team teamInDb = new Team();
        Set<User> employees = new HashSet<>();
        User teamLeader = new User();
        teamLeader.setTeam(teamInDb);
        employees.add(teamLeader);
        User employee1 = new User();
        employee1.setTeam(teamInDb);
        employees.add(employee1);
        User employee2 = new User();
        employee2.setTeam(teamInDb);
        employees.add(employee2);
        teamInDb.setEmployees(employees);

        when(teamRepository.findById(any()))
                .thenReturn(Optional.of(teamInDb));

        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());

        service.removeEmployeeFromTeam("id","id","moderatorName");
    }
}
