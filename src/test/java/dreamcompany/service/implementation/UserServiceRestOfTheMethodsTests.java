package dreamcompany.service.implementation;

import dreamcompany.domain.entity.*;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.error.notexist.TaskNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.TaskRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.LogService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

//repository logic not tested here assuming it works correctly for now
@RunWith(MockitoJUnitRunner.class)
public class UserServiceRestOfTheMethodsTests {


    private static final String USERNAME = "ivo";
    private static final String ANOTHER_USERNAME = "pesho";
    private static final String PASSWORD = "123";
    private static final String ANOTHER_PASSWORD = "12345";
    private static final String FIRST_NAME = "Ивайло";
    private static final String ANOTHER_FIRST_NAME = "Петър";
    private static final String LAST_NAME = "Николов";
    private static final String ANOTHER_LAST_NAME = "Владимиров";
    private static final String EMAIL = "ivailo.8.1993@abv.bg";
    private static final String ANOTHER_EMAIL = "pesho.8.1993@abv.bg";

    private static final String RANDOM_ID = "asidjasud";

    private static final String TASK_NAME = "Front end magic";
    private static final String TASK_DESCRIPTION = "Do some magic buddy";
    private static final Integer TASK_CREDITS = 10;

    public static final String ASSIGNED_TASK_LOG_MESSAGE = "Assigned task with name: "
            + TASK_NAME + " to user with username: "
            + USERNAME + " successfully";

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private LogService logService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper modelMapper;

    private User userInDb;

    private Task taskInDb;

    @Before
    public void init() {

        userInDb = new User();
        userInDb.setUsername(USERNAME);
        userInDb.setPassword(PASSWORD);
        userInDb.setFirstName(FIRST_NAME);
        userInDb.setLastName(LAST_NAME);
        userInDb.setEmail(EMAIL);
        userInDb.setPosition(Position.INTERN);

        ModelMapper actualMapper = new ModelMapper();

        when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenAnswer(invocationOnMock -> actualMapper.map(invocationOnMock.getArguments()[0], UserServiceModel.class));

        when(modelMapper.map(any(UserServiceModel.class), eq(User.class)))
                .thenAnswer(invocationOnMock -> actualMapper.map(invocationOnMock.getArguments()[0], User.class));

        when(modelMapper.map(any(Team.class), eq(TeamServiceModel.class)))
                .thenAnswer(invocationOnMock -> actualMapper.map(invocationOnMock.getArguments()[0], TeamServiceModel.class));

        when(modelMapper.map(any(Project.class), eq(ProjectServiceModel.class)))
                .thenAnswer(invocationOnMock -> actualMapper.map(invocationOnMock.getArguments()[0], ProjectServiceModel.class));

    }


    @Test
    public void findByUsername_shouldFindCorrect_whenUsernameExist() {

        //Arrange
        // make sure repository returns our user
        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(userInDb));

        //Act
        UserServiceModel actual = userService.findByUsername(USERNAME);

        //Assert
        assertEquals(userInDb.getUsername(), actual.getUsername());
        assertEquals(userInDb.getEmail(), actual.getEmail());
        assertEquals(userInDb.getFirstName(), actual.getFirstName());
        assertEquals(userInDb.getLastName(), actual.getLastName());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_shouldThrowException_whenUsernameNotExists() {
        //Arrange
        // make sure repository returns our user
        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.empty());

        //Act
        userService.findByUsername(USERNAME);
    }

    @Test
    public void findAll_shouldReturnCorrect() {

        //Arrange
        when(userRepository.findAll()).thenReturn(List.of(userInDb));

        //Act
        List<UserServiceModel> users = userService.findAll();

        //Assert
        assertEquals(1, users.size());
        assertEquals(USERNAME, users.get(0).getUsername());
    }

    @Test
    public void findAll_shouldReturnEmptyCollection_whenNoUsersInDb() {

        //Arrange
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAll();

        //Assert
        assertEquals(0, users.size());
    }

    @Test
    public void findAllNonLeadersWithoutTeam_shouldReturnCorrect() {

        //Arrange
        User anotherUser = new User();
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setEmail(ANOTHER_EMAIL);
        anotherUser.setPassword(ANOTHER_PASSWORD);
        anotherUser.setFirstName(ANOTHER_FIRST_NAME);
        anotherUser.setLastName(ANOTHER_LAST_NAME);
        anotherUser.setPosition(Position.INTERN);

        when(userRepository.findAllByTeamNullAndPositionNotIn(Position.TEAM_LEADER, Position.PROJECT_MANAGER))
                .thenReturn(List.of(userInDb, anotherUser));

        //Act
        List<UserServiceModel> users = userService.findAllNonLeadersWithoutTeam();

        //Assert
        assertEquals(2, users.size());
    }

    @Test
    public void findAllNonLeadersWithoutTeam_shouldReturnEmptyList_whenNoSuchUsers() {

        //Arrange
        when(userRepository.findAllByTeamNullAndPositionNotIn(Position.TEAM_LEADER, Position.PROJECT_MANAGER))
                .thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAllNonLeadersWithoutTeam();

        //Assert
        assertEquals(0, users.size());
    }

    @Test
    public void findAllWithoutTeam_shouldReturnCorrect() {

        //Arrange
        User anotherUser = new User();
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setEmail(ANOTHER_EMAIL);
        anotherUser.setPassword(ANOTHER_PASSWORD);
        anotherUser.setFirstName(ANOTHER_FIRST_NAME);
        anotherUser.setLastName(ANOTHER_LAST_NAME);

        when(userRepository.findAllByTeamNullAndPositionNotIn(Position.PROJECT_MANAGER))
                .thenReturn(List.of(userInDb, anotherUser));

        //Act
        List<UserServiceModel> users = userService.findAllWithoutTeam();

        //Assert
        assertEquals(2, users.size());
    }

    @Test
    public void findAllWithoutTeam_shouldReturnEmptyList_whenNoSuchUsersInDb() {

        //Arrange
        when(userRepository.findAllByTeamNullAndPositionNotIn(Position.PROJECT_MANAGER))
                .thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAllWithoutTeam();

        //Assert
        assertEquals(0, users.size());
    }

    @Test
    public void findAllInTeam_shouldReturnCorrect() {

        //Arrange
        User anotherUser = new User();
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setEmail(ANOTHER_EMAIL);
        anotherUser.setPassword(ANOTHER_PASSWORD);
        anotherUser.setFirstName(ANOTHER_FIRST_NAME);
        anotherUser.setLastName(ANOTHER_LAST_NAME);

        when(userRepository.findAllByTeamId(any()))
                .thenReturn(List.of(userInDb, anotherUser));

        //Act
        List<UserServiceModel> users = userService.findAllInTeam(RANDOM_ID);

        //Assert
        assertEquals(2, users.size());
    }

    @Test
    public void findAllInTeam_shouldReturnEmptyList_whenNoSuchUsersInDb() {

        //Arrange
        when(userRepository.findAllByTeamId(any()))
                .thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAllInTeam(RANDOM_ID);

        //Assert
        assertEquals(0, users.size());
    }


    @Test
    public void findAllInTeamWithPosition_shouldReturnCorrect() {

        //Arrange
        User anotherUser = new User();
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setEmail(ANOTHER_EMAIL);
        anotherUser.setPassword(ANOTHER_PASSWORD);
        anotherUser.setFirstName(ANOTHER_FIRST_NAME);
        anotherUser.setLastName(ANOTHER_LAST_NAME);
        anotherUser.setPosition(Position.INTERN);

        when(userRepository.findAllByTeamIdAndPosition(any(), any()))
                .thenReturn(List.of(userInDb, anotherUser));

        //Act
        List<UserServiceModel> users = userService.findAllInTeamWithPosition(RANDOM_ID, Position.INTERN);

        //Assert
        assertEquals(2, users.size());
    }

    @Test
    public void findAllInTeamWithPosition_shouldReturnEmptyList_whenNoSuchUsersInDb() {

        //Arrange
        when(userRepository.findAllByTeamIdAndPosition(any(), any()))
                .thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAllInTeamWithPosition(RANDOM_ID, Position.INTERN);

        //Assert
        assertEquals(0, users.size());
    }

    @Test
    public void findAllForPromotion_shouldReturnCorrect() {

        //Arrange
        User anotherUser = new User();
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setEmail(ANOTHER_EMAIL);
        anotherUser.setPassword(ANOTHER_PASSWORD);
        anotherUser.setFirstName(ANOTHER_FIRST_NAME);
        anotherUser.setLastName(ANOTHER_LAST_NAME);
        anotherUser.setPosition(Position.INTERN);

        when(userRepository.findAllByCreditsGreaterThanAndPositionNotIn(any(), any()))
                .thenReturn(List.of(userInDb, anotherUser));

        //Act
        List<UserServiceModel> users = userService.findAllForPromotion();

        //Assert
        assertEquals(2, users.size());
    }

    @Test
    public void findAllForPromotion_shouldReturnEmptyList_whenNoSuchUsersInDb() {

        //Arrange
        when(userRepository.findAllByCreditsGreaterThanAndPositionNotIn(any(), any()))
                .thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAllForPromotion();

        //Assert
        assertEquals(0, users.size());
    }

    @Test
    public void findAllForDemotion_shouldReturnCorrect() {

        //Arrange
        User anotherUser = new User();
        anotherUser.setUsername(ANOTHER_USERNAME);
        anotherUser.setEmail(ANOTHER_EMAIL);
        anotherUser.setPassword(ANOTHER_PASSWORD);
        anotherUser.setFirstName(ANOTHER_FIRST_NAME);
        anotherUser.setLastName(ANOTHER_LAST_NAME);
        anotherUser.setPosition(Position.INTERN);

        when(userRepository.findAllByPositionNotIn(any()))
                .thenReturn(List.of(userInDb, anotherUser));

        //Act
        List<UserServiceModel> users = userService.findAllForDemotion();

        //Assert
        assertEquals(2, users.size());
    }

    @Test
    public void findAllForDemotion_shouldReturnEmptyList_whenNoSuchUsersInDb() {

        //Arrange
        when(userRepository.findAllByPositionNotIn(any()))
                .thenReturn(new ArrayList<>());

        //Act
        List<UserServiceModel> users = userService.findAllForDemotion();

        //Assert
        assertEquals(0, users.size());
    }

    @Test
    public void isLeaderWithAssignedProject_shouldReturnTrue_whenTheUserIsATeamLeaderAndHasAProject() {

        //Arrange
        Team team = new Team();
        Project project = new Project();
        team.setProject(project);
        userInDb.setPosition(Position.TEAM_LEADER);
        userInDb.setTeam(team);

        UserServiceModel userServiceModel = modelMapper.map(userInDb, UserServiceModel.class);
        ProjectServiceModel projectServiceModel = modelMapper.map(project, ProjectServiceModel.class);
        TeamServiceModel teamServiceModel = modelMapper.map(team, TeamServiceModel.class);
        teamServiceModel.setProject(projectServiceModel);
        userServiceModel.setTeam(teamServiceModel);

        userInDb = modelMapper.map(userServiceModel, User.class);

        //in order service method to work correctly we had to do it
        when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenReturn(userServiceModel);

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(userInDb));

        //Act
        boolean actual = userService.isLeaderWithAssignedProject(USERNAME);

        //Assert
        assertTrue(actual);
    }

    @Test
    public void isLeaderWithAssignedProject_shouldReturnFalse_whenTheUserIsNoTeamLeader() {

        //Arrange
        Team team = new Team();
        Project project = new Project();
        team.setProject(project);
        userInDb.setTeam(team);

        UserServiceModel userServiceModel = modelMapper.map(userInDb, UserServiceModel.class);
        ProjectServiceModel projectServiceModel = modelMapper.map(project, ProjectServiceModel.class);
        TeamServiceModel teamServiceModel = modelMapper.map(team, TeamServiceModel.class);
        teamServiceModel.setProject(projectServiceModel);
        userServiceModel.setTeam(teamServiceModel);

        userInDb = modelMapper.map(userServiceModel, User.class);

        //in order service method to work correctly we had to do it
        when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenReturn(userServiceModel);

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(userInDb));

        //Act
        boolean actual = userService.isLeaderWithAssignedProject(USERNAME);

        //Assert
        assertFalse(actual);
    }

    @Test
    public void isLeaderWithAssignedProject_shouldReturnFalse_whenTheUserIsTeamLeaderButHasNoProject() {

        //Arrange
        Team team = new Team();
        userInDb.setTeam(team);
        userInDb.setPosition(Position.TEAM_LEADER);

        UserServiceModel userServiceModel = modelMapper.map(userInDb, UserServiceModel.class);
        TeamServiceModel teamServiceModel = modelMapper.map(team, TeamServiceModel.class);
        userServiceModel.setTeam(teamServiceModel);

        userInDb = modelMapper.map(userServiceModel, User.class);

        //in order service method to work correctly we had to do it
        when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenReturn(userServiceModel);

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(userInDb));

        //Act
        boolean actual = userService.isLeaderWithAssignedProject(USERNAME);

        //Assert
        assertFalse(actual);
    }

    @Test
    public void isLeaderWithAssignedProject_shouldReturnFalse_whenTheUserIsNoTeamLeaderAndHasNoProject() {

        //Arrange
        Team team = new Team();
        userInDb.setTeam(team);

        UserServiceModel userServiceModel = modelMapper.map(userInDb, UserServiceModel.class);
        TeamServiceModel teamServiceModel = modelMapper.map(team, TeamServiceModel.class);
        userServiceModel.setTeam(teamServiceModel);

        userInDb = modelMapper.map(userServiceModel, User.class);

        //in order service method to work correctly we had to do it
        when(modelMapper.map(any(User.class), eq(UserServiceModel.class)))
                .thenReturn(userServiceModel);

        when(userRepository.findByUsername(USERNAME))
                .thenReturn(Optional.of(userInDb));

        //Act
        boolean actual = userService.isLeaderWithAssignedProject(USERNAME);

        //Assert
        assertFalse(actual);
    }


    //happy case
    @Test
    public void assignTask_shouldWorkCorrectly_whenCalledWithValidParams() {

        // Arrange
        taskInDb = new Task();
        taskInDb.setId(RANDOM_ID);
        taskInDb.setName(TASK_NAME);
        taskInDb.setCredits(TASK_CREDITS);
        taskInDb.setDescription(TASK_DESCRIPTION);

        userInDb.setPosition(Position.TEAM_LEADER);
        Team team = new Team();
        team.setEmployees(new HashSet<>(List.of(userInDb)));
        userInDb.setTeam(team);

        // make sure repository find method returns our task
        when(taskRepository.findById(RANDOM_ID))
                .thenReturn(Optional.of(taskInDb));

        // make sure repository find method returns our user
        when(userRepository.findById(RANDOM_ID))
                .thenReturn(Optional.of(userInDb));

        // make sure log service create log method returns the message
        final String[] actualMessage = new String[1];
        when(logService.create(any()))
                .thenAnswer(invocationOnMock -> {
                    LogServiceModel logServiceModel = ((LogServiceModel) invocationOnMock.getArguments()[0]);
                    actualMessage[0] = logServiceModel.getDescription();
                    return logServiceModel.getDescription();
                });

        //Act
        userService.assignTask(RANDOM_ID, RANDOM_ID);

        //Assert
        assertEquals(Status.IN_PROGRESS.name(), taskInDb.getStatus().name());
        assertEquals(taskInDb.getEmployee().getUsername(),userInDb.getUsername());
        assertEquals(ASSIGNED_TASK_LOG_MESSAGE,actualMessage[0]);
    }


    @Test(expected = TaskNotFoundException.class)
    public void assignTask_shouldThrowException_whenCalledWithInvalidTaskIdParam() {

        // Arrange
        taskInDb = new Task();
        taskInDb.setId(RANDOM_ID);
        taskInDb.setName(TASK_NAME);
        taskInDb.setCredits(TASK_CREDITS);
        taskInDb.setDescription(TASK_DESCRIPTION);

        // make sure repository find method returns empty optional
        when(taskRepository.findById(RANDOM_ID))
                .thenReturn(Optional.empty());

        // make sure repository find method returns our user
        when(userRepository.findById(RANDOM_ID))
                .thenReturn(Optional.of(userInDb));

        //Act
        userService.assignTask(RANDOM_ID, RANDOM_ID);
    }

    @Test(expected = UserNotFoundException.class)
    public void assignTask_shouldThrowException_whenCalledWithInvalidUserIdParam() {

        // Arrange
        // make sure repository find method returns empty optional
        when(userRepository.findById(RANDOM_ID))
                .thenReturn(Optional.empty());

        //Act
        userService.assignTask(RANDOM_ID, RANDOM_ID);
    }
}
