package dreamcompany.service.implementation.task;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.error.duplicates.TaskNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidTaskServiceModelException;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.error.notexist.TaskNotFoundException;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TaskRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.validation.TaskValidationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class TaskServiceCreateAndEditTests extends TestBase {

    @Autowired
    TaskService taskService;

    @MockBean
    TaskValidationService taskValidationService;

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    ProjectRepository projectRepository;

    TaskServiceModel task;

    @Override
    protected void before() {
        task = new TaskServiceModel();

        when(taskValidationService.isValid(any()))
                .thenReturn(true);
    }

    @Test
    public void create_shouldCreateTaskWithPendingStatus_whenValidModel() {

        when(taskRepository.existsByNameAndProjectId(any(), any()))
                .thenReturn(false);

        task.setName("TaskName");
        task.setProject("randomProjectId");

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(new Project()));


        when(taskRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        TaskServiceModel saved = taskService.create(task);

        //Assert
        verify(taskRepository).save(any());
        assertEquals(Status.PENDING, saved.getStatus());
        assertEquals("TaskName", saved.getName());
    }

    @Test(expected = InvalidTaskServiceModelException.class)
    public void create_shouldThrowException_whenModelNotValid() {

        when(taskValidationService.isValid(any()))
                .thenReturn(false);

        taskService.create(task);
    }

    @Test(expected = TaskNameAlreadyExistException.class)
    public void create_shouldThrowException_whenTaskWithThatNameExistsInThatProject() {

        when(taskRepository.existsByNameAndProjectId(any(), any()))
                .thenReturn(true);

        taskService.create(task);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void create_shouldThrowException_whenCreatedForInvalidProject() {

        when(taskRepository.existsByNameAndProjectId(any(), any()))
                .thenReturn(false);

        task.setName("TaskName");
        task.setProject("randomInvalidProjectId");

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        taskService.create(task);
    }

    @Test
    public void edit_shouldEditCorrectly_whenTaskExistsAndEditedModelIsValid() {

        Task taskInDB = new Task();
        Project project = new Project();
        project.setId("OldProjectId");

        taskInDB.setName("OldName");
        taskInDB.setProject(project);
        taskInDB.setDescription("OldDescription");
        taskInDB.setCredits(1);
        taskInDB.setRequiredPosition(Position.INTERN);
        taskInDB.setMinutesNeeded(1);

        when(taskRepository.findById(any()))
                .thenReturn(Optional.of(taskInDB));

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(project));

        TaskServiceModel edited = new TaskServiceModel();
        edited.setName("NewName");
        edited.setMinutesNeeded(2);
        edited.setRequiredPosition(Position.JUNIOR);
        edited.setDescription("NewDescription");
        edited.setCredits(2);
        edited.setProject("NewProjectId");

        when(taskRepository.existsByNameAndProjectId(any(), any()))
                .thenReturn(false);

        when(taskRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        TaskServiceModel editedAndSaved = taskService.edit("taskId", edited);

        //Assert
        verify(taskRepository).save(any());
        assertEquals("NewName", editedAndSaved.getName());
        assertEquals(2, editedAndSaved.getMinutesNeeded());
        assertEquals(Position.JUNIOR, editedAndSaved.getRequiredPosition());
        assertEquals("NewDescription", editedAndSaved.getDescription());
        assertEquals(2, editedAndSaved.getCredits().intValue());
        assertEquals(project.toString(), editedAndSaved.getProject());
    }

    @Test(expected = InvalidTaskServiceModelException.class)
    public void edit_shouldThrowException_whenInvalidEditedModel() {

        when(taskValidationService.isValid(any()))
                .thenReturn(false);

        taskService.edit("taskId", new TaskServiceModel());
    }

    @Test(expected = TaskNameAlreadyExistException.class)
    public void edit_shouldThrowException_whenEditedTaskNameAlreadyExistsInThatProject() {

        when(taskRepository.existsByNameAndProjectId(any(), any()))
                .thenReturn(true);

        Task taskInDB = new Task();
        taskInDB.setName("TaskName");
        Project project = new Project();
        project.setId("OldProjectId");

        when(taskRepository.findById(any()))
                .thenReturn(Optional.of(taskInDB));

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(project));

        task.setName("NewTaskName");

        taskService.edit("taskId", task);
    }

    @Test(expected = TaskNotFoundException.class)
    public void edit_shouldThrowException_whenInvalidTaskId() {

        when(taskRepository.findById(any()))
                .thenReturn(Optional.empty());

        taskService.edit("taskId", task);

    }

    @Test(expected = ProjectNotFoundException.class)
    public void edit_shouldThrowException_whenInvalidProjectId() {

        when(taskRepository.findById(any()))
                .thenReturn(Optional.of(new Task()));

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        taskService.edit("taskId", task);
    }
}
