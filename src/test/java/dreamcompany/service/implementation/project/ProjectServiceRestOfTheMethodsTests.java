package dreamcompany.service.implementation.project;

import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.entity.Team;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TaskRepository;
import dreamcompany.repository.TeamRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.ProjectService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProjectServiceRestOfTheMethodsTests extends TestBase {

    @Autowired
    ProjectService projectService;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    TaskRepository taskRepository;

    @MockBean
    TeamRepository teamRepository;

    @Test
    public void delete_shouldDeleteProjectFromDb_whenValidId() {

        Project projectInDb = new Project();
        projectInDb.setName("projectName");

        Team team = new Team();

        projectInDb.setTeam(team);

        when(projectRepository.findById(anyString()))
                .thenReturn(Optional.of(projectInDb));

        ProjectServiceModel deletedProject = projectService.delete("projectId");

        verify(teamRepository).save(team);
        verify(projectRepository).delete(projectInDb);
        assertEquals(projectInDb.getName(), deletedProject.getName());
    }

    @Test(expected = ProjectNotFoundException.class)
    public void delete_shouldThrowException_whenInvalidProjectId() {

        when(projectRepository.findById(anyString()))
                .thenReturn(Optional.empty());

        projectService.delete("projectId");
    }

    @Test
    public void findAll_shouldReturnAllProjectsInDb_whenAny() {

        List<Project> projectsInDb = List.of(new Project(), new Project());

        when(projectRepository.findAll())
                .thenReturn(projectsInDb);

        List<ProjectServiceModel> returned = projectService.findAll();

        assertEquals(projectsInDb.size(), returned.size());
    }

    @Test
    public void findAllNotCompleted_shouldReturnAllNotFinishedProjectsInDb_whenAny() {

        List<Project> projectsInDb = List.of(new Project(), new Project());

        when(projectRepository.findAllByStatusNotIn(eq(Status.FINISHED)))
                .thenReturn(projectsInDb);

        List<ProjectServiceModel> returned = projectService.findAllNotCompleted();

        assertEquals(projectsInDb.size(), returned.size());
    }

    @Test
    public void findAllByStatus_shouldReturnAllProjectsInDBWithSelectedStatus_whenStatusIsAll() {

        List<Project> projectsInDb = List.of(new Project(), new Project());

        when(projectRepository.findAll())
                .thenReturn(projectsInDb);

        List<ProjectServiceModel> returned = projectService.findAllByStatus("all");

        assertEquals(projectsInDb.size(), returned.size());
    }

    @Test
    public void findAllByStatus_shouldReturnAllProjectsInDBWithSelectedStatus_whenStatusIsPending() {

        List<Project> projectsInDb = List.of(new Project(), new Project());

        when(projectRepository.findAllByStatus(Status.PENDING))
                .thenReturn(projectsInDb);

        List<ProjectServiceModel> returned = projectService.findAllByStatus("pending");

        assertEquals(projectsInDb.size(), returned.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllByStatus_shouldThrowException_whenStatusIsInvalid() {
        projectService.findAllByStatus("dead");
    }

    @Test
    public void findById_shouldReturnProjectWithThatIdFromDB_whenValidId() {

        Project project = new Project();
        project.setName("projectName");

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(project));

        ProjectServiceModel returned = projectService.findById("projectId");

        assertEquals(project.getName(), returned.getName());
    }

    @Test(expected = ProjectNotFoundException.class)
    public void findById_shouldThrowException_whenInvalidId() {

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        projectService.findById("projectId");
    }

    @Test
    public void projectIsCompleted_shouldReturnTrue_whenAllTasksInProjectsAreWithCompletedStatus() {

        Project projectInDb = new Project();
        projectInDb.setTasks(new HashSet<>());
        projectInDb.getTasks().add(new Task());

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(projectInDb));

        when(taskRepository.countAllByStatusAndProjectId(eq(Status.FINISHED), eq("projectId")))
                .thenReturn(Long.valueOf(projectInDb.getTasks().size()));

        assertTrue(projectService.projectIsCompleted("projectId"));
    }

    @Test
    public void projectIsCompleted_shouldReturnFalse_whenNotAllTasksInProjectsAreWithCompletedStatus() {

        Project projectInDb = new Project();
        projectInDb.setTasks(new HashSet<>());
        projectInDb.getTasks().add(new Task());
        projectInDb.getTasks().add(new Task());

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(projectInDb));

        when(taskRepository.countAllByStatusAndProjectId(eq(Status.FINISHED), eq("projectId")))
                .thenReturn(Long.valueOf(projectInDb.getTasks().size() / 2));

        assertFalse(projectService.projectIsCompleted("projectId"));
    }

    @Test(expected = ProjectNotFoundException.class)
    public void projectIsCompleted_shouldThrowException_whenInvalidProjectId(){

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        projectService.projectIsCompleted("projectId");
    }

    @Test
    public void complete_shouldSetProjectStatusToFinishedAndAddProjectRewardToTeamProfit_whenAllTasksInProjectsAreWithCompletedStatus() {

        Project projectInDb = new Project();
        projectInDb.setStatus(Status.IN_PROGRESS);
        projectInDb.setReward(BigDecimal.valueOf(1000));
        Team team = new Team();
        team.setProject(projectInDb);
        team.setProfit(BigDecimal.ZERO);
        projectInDb.setTasks(new HashSet<>());
        projectInDb.getTasks().add(new Task());
        projectInDb.setTeam(team);

        when(projectRepository.findById(any()))
                .thenReturn(Optional.of(projectInDb));

        when(taskRepository.countAllByStatusAndProjectId(eq(Status.FINISHED), eq("projectId")))
                .thenReturn(Long.valueOf(projectInDb.getTasks().size()));

        when(projectRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(teamRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Act
        projectService.complete("projectId");

        //Assert
        assertEquals(Status.FINISHED,projectInDb.getStatus());
        assertEquals(BigDecimal.valueOf(1000),team.getProfit());
        assertNull(team.getProject());
    }

    @Test(expected = ProjectNotFoundException.class)
    public void complete_shouldThrowException_whenInvalidProjectId(){

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        projectService.complete("projectId");
    }
}
