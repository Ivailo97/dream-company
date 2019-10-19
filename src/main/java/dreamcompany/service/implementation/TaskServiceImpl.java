package dreamcompany.service.implementation;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Project;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TaskRepository;
import dreamcompany.service.interfaces.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskServiceModel create(TaskServiceModel taskServiceModel) {

        Task task = modelMapper.map(taskServiceModel, Task.class);

        Project project = projectRepository.findById(taskServiceModel.getProject())
                .orElseThrow(() -> new IllegalArgumentException("Invalid project Id"));

        task.setProject(project);

        task.setCreatedOn(LocalDateTime.now());
        task.setStatus(Status.PENDING);

        task = taskRepository.save(task);
        return modelMapper.map(task, TaskServiceModel.class);
    }

    @Override
    public TaskServiceModel edit(String id, TaskServiceModel taskServiceModel) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        Project project = projectRepository.findById(taskServiceModel.getProject())
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));

        task.setProject(project);
        task.setDescription(taskServiceModel.getDescription());
        task.setCredits(taskServiceModel.getCredits());
        task.setName(taskServiceModel.getName());
        task.setRequiredPosition(taskServiceModel.getRequiredPosition());
        task.setMinutesNeeded(taskServiceModel.getMinutesNeeded());

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

        if (status.equals("all")){
            return  taskRepository.findAllByProjectId(projectId).stream()
                    .map(t -> modelMapper.map(t, TaskServiceModel.class))
                    .collect(Collectors.toList());
        }

        return taskRepository.findAllByProjectIdAndStatus(projectId, Status.valueOf(status.toUpperCase()))
                .stream()
                .map(t -> modelMapper.map(t, TaskServiceModel.class))
                .collect(Collectors.toList());
    }
}
