package dreamcompany.service.implementation.task;

import dreamcompany.domain.entity.*;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.error.notexist.TaskNotFoundException;
import dreamcompany.error.notexist.TeamNotFoundException;
import dreamcompany.repository.TaskRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.TaskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TaskServiceRestOfTheMethodsTests extends TestBase {

    @Autowired
    TaskService taskService;

    @MockBean
    TaskRepository taskRepository;


    @Test
    public void delete_shouldDeleteTask_whenValidTaskId() {

        Task taskInDb = new Task();
        taskInDb.setName("TaskName");
        taskInDb.setStatus(Status.PENDING);
        taskInDb.setCredits(10);

        when(taskRepository.findById(any()))
                .thenReturn(Optional.of(taskInDb));

        //Act
        TaskServiceModel deletedTask = taskService.delete("taskId");

        //Assert
        verify(taskRepository).delete(any());
        assertEquals("TaskName", deletedTask.getName());
        assertEquals(Status.PENDING, deletedTask.getStatus());
        assertEquals(10, deletedTask.getCredits().intValue());
    }

    @Test(expected = TaskNotFoundException.class)
    public void delete_shouldThrowException_whenInvalidTaskId() {

        when(taskRepository.findById(any()))
                .thenReturn(Optional.empty());

        taskService.delete("taskId");
    }


    @Test
    public void findById_shouldSetTaskProjectNameAndReturnCorrect_whenValidTaskId() {

        Task taskInDb = new Task();
        taskInDb.setName("TaskName");
        Project project = new Project();
        project.setName("ProjectName");
        taskInDb.setProject(project);

        when(taskRepository.findById(any()))
                .thenReturn(Optional.of(taskInDb));

        //Act
        TaskServiceModel foundAndReturned = taskService.findById("taskId");

        //Assert
        assertEquals("TaskName", foundAndReturned.getName());
        assertEquals("ProjectName", foundAndReturned.getProject());
    }

    @Test(expected = TaskNotFoundException.class)
    public void findById_shouldThrowException_whenInvalidTaskId() {

        when(taskRepository.findById(any()))
                .thenReturn(Optional.empty());

        taskService.findById("taskId");
    }


    @Test
    public void findAll_shouldReturnAllTasksInDb_whenAny() {

        List<Task> tasksInDb = List.of(new Task(), new Task());

        when(taskRepository.findAll())
                .thenReturn(tasksInDb);

        List<TaskServiceModel> returned = taskService.findAll();

        assertEquals(tasksInDb.size(), returned.size());
    }

    @Test
    public void findAllNonAssignedByProjectId_shouldReturnAllTasksInAProjectWhichAreNotAssigned_whenAny() {

        List<Task> tasksInDb = List.of(new Task(), new Task());

        when(taskRepository.findAllByEmployeeNullAndProjectId(any()))
                .thenReturn(tasksInDb);

        List<TaskServiceModel> returned = taskService.findAllNonAssignedByProjectId("projectId");

        assertEquals(tasksInDb.size(), returned.size());
    }

    @Test
    public void findRequiredPosition_shouldReturnRequiredPositionForATask_whenValidTaskId() {

        Task taskInDb = new Task();
        taskInDb.setRequiredPosition(Position.JUNIOR);

        when(taskRepository.findById("taskId"))
                .thenReturn(Optional.of(taskInDb));

        Position position = taskService.findRequiredPosition("taskId");

        assertEquals(Position.JUNIOR, position);
    }

    @Test(expected = TaskNotFoundException.class)
    public void findRequiredPosition_shouldThrowException_whenInvalidTaskId() {

        when(taskRepository.findById("taskId"))
                .thenReturn(Optional.empty());
        taskService.findRequiredPosition("taskId");
    }


    @Test
    public void findTeamId_shouldReturnTeamIdOfTheTaskProject_whenIsPresent() {

        Task taskInDb = new Task();
        taskInDb.setId("taskId");

        Team taskProjectTeam = new Team();
        taskProjectTeam.setId("teamId");

        Project project = new Project();
        project.setName("projectName");

        project.setTeam(taskProjectTeam);
        taskInDb.setProject(project);

        when(taskRepository.findById("taskId"))
                .thenReturn(Optional.of(taskInDb));

        String returnedTeamId = taskService.findTeamId("taskId");

        assertEquals("teamId", returnedTeamId);
    }

    @Test(expected = TaskNotFoundException.class)
    public void findTeamId_shouldThrowException_whenInvalidTaskId() {

        when(taskRepository.findById("taskId"))
                .thenReturn(Optional.empty());

        taskService.findTeamId("taskId");
    }

    @Test(expected = TeamNotFoundException.class)
    public void findTeamId_shouldThrowException_whenTeamNotPresent() {

        Task taskInDb = new Task();
        taskInDb.setId("taskId");

        Project project = new Project();
        project.setName("projectName");

        taskInDb.setProject(project);

        when(taskRepository.findById("taskId"))
                .thenReturn(Optional.of(taskInDb));

        taskService.findTeamId("taskId");
    }

    @Test
    public void findNotFinishedAssignedToUser_shouldReturnAllWithStatusInProgressAssignedToAUser_whenValidUserId() {

        List<Task> tasksInDb = List.of(new Task(), new Task());

        when(taskRepository.findAllByEmployeeIdAndStatus(any(), eq(Status.IN_PROGRESS)))
                .thenReturn(tasksInDb);

        List<TaskServiceModel> returned = taskService.findNotFinishedAssignedToUser("userId");

        assertEquals(tasksInDb.size(), returned.size());
    }

    @Test
    public void findAllByProjectIdAndStatus_shouldReturnAllInTheProject_whenCalledWithAllStatus() {

        List<Task> tasksInDb = List.of(new Task(), new Task());

        when(taskRepository.findAllByProjectId("projectId"))
                .thenReturn(tasksInDb);

        List<TaskServiceModel> returnedTasks = taskService.findAllByProjectIdAndStatus("projectId", "all");

        assertEquals(tasksInDb.size(), returnedTasks.size());
    }

    @Test
    public void findAllByProjectIdAndStatus_shouldReturnAllInTheProject_whenCalledWithAnotherValidStatus() {

        List<Task> tasksInDb = List.of(new Task(), new Task());

        when(taskRepository.findAllByProjectIdAndStatus("projectId",Status.PENDING))
                .thenReturn(tasksInDb);

        List<TaskServiceModel> returnedTasks = taskService.findAllByProjectIdAndStatus("projectId", "pending");

        assertEquals(tasksInDb.size(), returnedTasks.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void FindAllByProjectIdAndStatus_shouldThrowException_whenCalledWithInvalidStatus(){

        List<Task> tasksInDb = List.of(new Task(), new Task());

        when(taskRepository.findAllByProjectId("projectId"))
                .thenReturn(tasksInDb);

        taskService.findAllByProjectIdAndStatus("projectId","dead");
    }
}
