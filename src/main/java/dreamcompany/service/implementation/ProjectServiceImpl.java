package dreamcompany.service.implementation;

import dreamcompany.common.GlobalConstants;
import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.entity.Team;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.error.duplicates.ProjectNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidProjectServiceModelException;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TaskRepository;
import dreamcompany.repository.TeamRepository;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.validation.ProjectValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static dreamcompany.common.GlobalConstants.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final String STATUS_ALL = "all";

    private final ProjectRepository projectRepository;

    private final ProjectValidationService validationService;

    private final TeamRepository teamRepository;

    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    @Override
    public ProjectServiceModel create(ProjectServiceModel projectServiceModel) {

        throwIfInvalidServiceModel(projectServiceModel);

        if (projectRepository.existsByName(projectServiceModel.getName())) {
            throw new ProjectNameAlreadyExistException(GlobalConstants.DUPLICATE_PROJECT_MESSAGE);
        }

        projectServiceModel.setStatus(Status.PENDING);
        Project project = modelMapper.map(projectServiceModel, Project.class);
        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectServiceModel.class);
    }

    private void throwIfInvalidServiceModel(ProjectServiceModel projectServiceModel) {

        if (!validationService.isValid(projectServiceModel)) {
            throw new InvalidProjectServiceModelException(INVALID_PROJECT_SERVICE_MODEL_MESSAGE);
        }
    }

    @Override
    public ProjectServiceModel edit(String id, ProjectServiceModel projectServiceModel) {

        throwIfInvalidServiceModel(projectServiceModel);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        if (!project.getName().equals(projectServiceModel.getName())) {

            if (projectRepository.existsByName(projectServiceModel.getName())) {
                throw new ProjectNameAlreadyExistException(GlobalConstants.DUPLICATE_PROJECT_MESSAGE);
            }
        }

        project.setName(projectServiceModel.getName());
        project.setDescription(projectServiceModel.getDescription());
        project.setReward(projectServiceModel.getReward());
        project = projectRepository.save(project);
        return modelMapper.map(project, ProjectServiceModel.class);
    }

    @Override
    public ProjectServiceModel delete(String id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        Team team = project.getTeam();

        if (team != null) {
            team.setProject(null);
            teamRepository.save(team);
        }

        ProjectServiceModel projectServiceModel = modelMapper.map(project, ProjectServiceModel.class);

        projectRepository.delete(project);

        return projectServiceModel;
    }

    @Override
    public List<ProjectServiceModel> findAll() {
        return projectRepository.findAll().stream()
                .map(x -> modelMapper.map(x, ProjectServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectServiceModel> findAllNotCompleted() {
        return projectRepository.findAllByStatusNotIn(Status.FINISHED).stream()
                .map(x -> modelMapper.map(x, ProjectServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectServiceModel> findAllByStatus(String statusName) {

        if (statusName.equals(STATUS_ALL)) {
            return projectRepository.findAll().stream()
                    .map(p -> modelMapper.map(p, ProjectServiceModel.class))
                    .collect(Collectors.toList());
        }

        Status status = Status.valueOf(statusName.toUpperCase());

        return projectRepository.findAllByStatus(status)
                .stream()
                .map(p -> modelMapper.map(p, ProjectServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectServiceModel findById(String id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        return modelMapper.map(project, ProjectServiceModel.class);
    }

    @Override
    public boolean projectIsCompleted(String id) {

        Set<Task> projectTasks = projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE))
                .getTasks();

        long completedTasks = taskRepository.countAllByStatusAndProjectId(Status.FINISHED, id);

        return projectTasks != null && projectTasks.size() == completedTasks;
    }

    @Override
    public void complete(String id) {

        if (projectIsCompleted(id)) {

            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

            project.setStatus(Status.FINISHED);

            projectRepository.save(project);

            Team assignedTeam = project.getTeam();

            assignedTeam.setProfit(assignedTeam.getProfit().add(project.getReward()));

            assignedTeam.setProject(null);

            teamRepository.save(assignedTeam);
        }
    }
}
