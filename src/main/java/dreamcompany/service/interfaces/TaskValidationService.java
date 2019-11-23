package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.TaskServiceModel;

public interface TaskValidationService {
    boolean isValid(TaskServiceModel task);
}
