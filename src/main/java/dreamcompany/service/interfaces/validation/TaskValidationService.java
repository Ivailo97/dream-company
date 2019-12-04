package dreamcompany.service.interfaces.validation;

import dreamcompany.domain.model.service.TaskServiceModel;

public interface TaskValidationService {
    boolean isValid(TaskServiceModel task);
}
