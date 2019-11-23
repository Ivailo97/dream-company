package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.entity.Team;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.error.duplicates.ProjectNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidProjectServiceModelException;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TaskRepository;
import dreamcompany.repository.TeamRepository;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.ProjectValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dreamcompany.GlobalConstraints.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectValidationService validationService;

    private final TeamRepository teamRepository;

    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectValidationService validationService, TeamRepository teamRepository, TaskRepository taskRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.validationService = validationService;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProjectServiceModel create(ProjectServiceModel projectServiceModel) {

        throwIfInvalidServiceModel(projectServiceModel);

        if (projectRepository.existsByName(projectServiceModel.getName())) {
            throw new ProjectNameAlreadyExistException(GlobalConstraints.DUPLICATE_PROJECT_MESSAGE);
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));

        if (!project.getName().equals(projectServiceModel.getName())) {

            if (projectRepository.existsByName(projectServiceModel.getName())) {
                throw new ProjectNameAlreadyExistException(GlobalConstraints.DUPLICATE_PROJECT_MESSAGE);
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
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));


        Team team = project.getTeam();

        project.setTeam(null);
        team.setProject(null);

        teamRepository.save(team);
        projectRepository.save(project);

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
    public List<ProjectServiceModel> findAllByStatus(String status) {

        if (status.equals("all")) {
            return projectRepository.findAll().stream()
                    .map(p -> modelMapper.map(p, ProjectServiceModel.class))
                    .collect(Collectors.toList());
        }

        return projectRepository.findAllByStatus(Status.valueOf(status.toUpperCase()))
                .stream()
                .map(p -> modelMapper.map(p, ProjectServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectServiceModel findById(String id) {

        Project project = projectRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));

        return modelMapper.map(project, ProjectServiceModel.class);
    }

    @Override
    public boolean projectIsCompleted(String id) {

        Set<Task> allTasks = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"))
                .getTasks();

        long completedTasks = taskRepository.countAllByStatusAndProjectId(Status.FINISHED, id);

        return allTasks != null && allTasks.size() == completedTasks;
    }

    @Override
    public void complete(String id) {

        if (projectIsCompleted(id)) {

            Project project = projectRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));

            project.setStatus(Status.FINISHED);

            projectRepository.save(project);

            Team assignedTeam = project.getTeam();

            assignedTeam.setProfit(assignedTeam.getProfit().add(project.getReward()));

            assignedTeam.setProject(null);

            teamRepository.save(assignedTeam);
        }
    }
}
