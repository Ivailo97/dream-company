package dreamcompany.service.interfaces;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.service.TaskServiceModel;

import java.util.List;

public interface TaskService {

    TaskServiceModel create(TaskServiceModel taskServiceModel);

    TaskServiceModel edit(String id, TaskServiceModel taskServiceModel);

    TaskServiceModel delete(String id);

    TaskServiceModel findById(String id);

    List<TaskServiceModel> findAll();

    List<TaskServiceModel> findAllNonAssignedByProjectId(String id);

    Position findRequiredPosition(String id);

    String findTeamId(String taskId);

    List<TaskServiceModel> findNotFinishedAssignedToUser(String userId);
}
