package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.ProjectServiceModel;

public interface ProjectValidationService {

    boolean isValid(ProjectServiceModel project);
}
