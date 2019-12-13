package dreamcompany.service.implementation.project;

import dreamcompany.domain.entity.Project;
import dreamcompany.domain.enumeration.Status;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.error.duplicates.ProjectNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidProjectServiceModelException;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TeamRepository;
import dreamcompany.service.implementation.base.TestBase;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.validation.ProjectValidationService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ProjectServiceCreateAndEditTests extends TestBase {

    @Autowired
    ProjectService projectService;

    @MockBean
    ProjectValidationService validationService;

    @MockBean
    ProjectRepository projectRepository;

    @MockBean
    TeamRepository teamRepository;

    ProjectServiceModel project;

    @Override
    protected void before() {
        project = new ProjectServiceModel();
        when(validationService.isValid(any()))
                .thenReturn(true);
    }

    @Test
    public void create_shouldSaveProjectInDb_whenValidModel() {

        project.setName("projectName");
        project.setDescription("projectDescription");
        project.setReward(BigDecimal.TEN);

        when(projectRepository.existsByName(any()))
                .thenReturn(false);

        when(projectRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectServiceModel savedAndReturned = projectService.create(project);

        verify(projectRepository).save(any());
        assertEquals(project.getName(), savedAndReturned.getName());
        assertEquals(project.getDescription(), savedAndReturned.getDescription());
        assertEquals(project.getReward(), savedAndReturned.getReward());
        assertEquals(Status.PENDING, savedAndReturned.getStatus());
    }


    @Test(expected = InvalidProjectServiceModelException.class)
    public void create_shouldThrowException_whenInvalidModel() {

        when(validationService.isValid(any()))
                .thenReturn(false);

        projectService.create(project);
    }

    @Test(expected = ProjectNameAlreadyExistException.class)
    public void create_shouldThrowException_whenProjectWithSameNameExists() {

        when(projectRepository.existsByName(any()))
                .thenReturn(true);

        projectService.create(project);
    }

    @Test
    public void edit_shouldUpdateExistingProject_whenValidEditedModelAndExistingProject() {

        Project projectInDb = new Project();
        projectInDb.setName("oldProjectName");
        projectInDb.setDescription("oldProjectDescription");
        projectInDb.setReward(BigDecimal.TEN);

        when(projectRepository.findById(anyString()))
                .thenReturn(Optional.of(projectInDb));

        project.setName("newProjectName");
        project.setDescription("newProjectDescription");
        project.setReward(BigDecimal.ONE);

        when(projectRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProjectServiceModel editedAndSaved = projectService.edit("projectId", project);

        verify(projectRepository).save(any());
        assertEquals(project.getName(), editedAndSaved.getName());
        assertEquals(project.getDescription(), editedAndSaved.getDescription());
        assertEquals(project.getReward(), editedAndSaved.getReward());
    }


    @Test(expected = InvalidProjectServiceModelException.class)
    public void edit_shouldThrowException_whenInvalidModel() {

        when(validationService.isValid(any()))
                .thenReturn(false);

        projectService.edit("projectId", project);
    }

    @Test(expected = ProjectNotFoundException.class)
    public void edit_shouldThrowException_whenInvalidProjectId() {

        when(projectRepository.findById(any()))
                .thenReturn(Optional.empty());

        projectService.edit("projectId", project);
    }

    @Test(expected = ProjectNameAlreadyExistException.class)
    public void edit_shouldThrow_whenValidEditedModelWithAlreadyTakenName() {

        Project projectInDb = new Project();
        projectInDb.setName("oldProjectName");

        when(projectRepository.findById(anyString()))
                .thenReturn(Optional.of(projectInDb));

        project.setName("newProjectName");

        when(projectRepository.existsByName(any()))
                .thenReturn(true);

        projectService.edit("projectId", project);
    }
}
