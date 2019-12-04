package dreamcompany.service.interfaces.validation;

import dreamcompany.domain.model.service.ProjectServiceModel;

public interface ProjectValidationService {

    boolean isValid(ProjectServiceModel project);
}
