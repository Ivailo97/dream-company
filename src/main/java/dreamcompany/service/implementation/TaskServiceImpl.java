package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.error.duplicates.TaskNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidTaskServiceModelException;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TaskRepository;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.validation.TaskValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static dreamcompany.GlobalConstraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskValidationService validationService;

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;

    @Override
    public TaskServiceModel create(TaskServiceModel taskServiceModel) {

        throwIfInvalidServiceModel(taskServiceModel);

        throwIfDuplicate(taskServiceModel);

        Task task = modelMapper.map(taskServiceModel, Task.class);

        Project project = projectRepository.findById(taskServiceModel.getProject())
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        task.setProject(project);

        task.setCreatedOn(LocalDateTime.now());
        task.setStatus(Status.PENDING);

        task = taskRepository.save(task);
        return modelMapper.map(task, TaskServiceModel.class);
    }

    private void throwIfInvalidServiceModel(TaskServiceModel taskServiceModel) {
        if (!validationService.isValid(taskServiceModel)) {
            throw new InvalidTaskServiceModelException(INVALID_TASK_SERVICE_MODEL_MESSAGE);
        }
    }

    @Override
    public TaskServiceModel edit(String id, TaskServiceModel edited) {

        throwIfInvalidServiceModel(edited);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        Project project = projectRepository.findById(edited.getProject())
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));

        if (!edited.getName().equals(task.getName())) {
            throwIfDuplicate(edited);
        }

        task.setProject(project);
        task.setDescription(edited.getDescription());
        task.setCredits(edited.getCredits());
        task.setName(edited.getName());
        task.setRequiredPosition(edited.getRequiredPosition());
        task.setMinutesNeeded(edited.getMinutesNeeded());

        task = taskRepository.save(task);

        return modelMapper.map(task, TaskServiceModel.class);
    }

    @Override
    public TaskServiceModel delete(String id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        TaskServiceModel taskServiceModel = modelMapper.map(task, TaskServiceModel.class);

        taskRepository.delete(task);

        return taskServiceModel;
    }

    @Override
    public TaskServiceModel findById(String id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        TaskServiceModel taskServiceModel = modelMapper.map(task, TaskServiceModel.class);
        taskServiceModel.setProject(task.getProject().getName());

        return taskServiceModel;
    }

    @Override
    public List<TaskServiceModel> findAll() {

        return taskRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TaskServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskServiceModel> findAllNonAssignedByProjectId(String id) {
        return taskRepository.findAllByEmployeeNullAndProjectId(id).stream()
                .map(t -> modelMapper.map(t, TaskServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public Position findRequiredPosition(String id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        return task.getRequiredPosition();
    }

    @Override
    public String findTeamId(String taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        return task.getProject().getTeam().getId();
    }

    @Override
    public List<TaskServiceModel> findNotFinishedAssignedToUser(String userId) {

        return taskRepository.findAllByEmployeeIdAndStatus(userId, Status.IN_PROGRESS)
                .stream()
                .map(t -> modelMapper.map(t, TaskServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskServiceModel> findAllByProjectIdAndStatus(String projectId, String status) {

        if (status.equals("all")) {
            return taskRepository.findAllByProjectId(projectId).stream()
                    .map(t -> modelMapper.map(t, TaskServiceModel.class))
                    .collect(Collectors.toList());
        }

        return taskRepository.findAllByProjectIdAndStatus(projectId, Status.valueOf(status.toUpperCase()))
                .stream()
                .map(t -> modelMapper.map(t, TaskServiceModel.class))
                .collect(Collectors.toList());
    }

    private void throwIfDuplicate(TaskServiceModel task) {

        if (taskRepository.existsByNameAndProjectId(task.getName(), task.getProject())) {
            throw new TaskNameAlreadyExistException(GlobalConstraints.DUPLICATE_TASK_MESSAGE);
        }
    }
}
