package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.service.interfaces.ProjectValidationService;
import org.springframework.stereotype.Service;

import static dreamcompany.validation.project.ProjectConstants.*;

@Service
public class ProjectValidationServiceImpl implements ProjectValidationService {

    @Override
    public boolean isValid(ProjectServiceModel project) {
        return project != null && fieldsAreValid(project);
    }

    private boolean fieldsAreValid(ProjectServiceModel project) {
        return fieldsAreNotNull(project) && fieldsMeetTheRequirements(project);
    }

    private boolean fieldsMeetTheRequirements(ProjectServiceModel project) {
        return project.getName().matches(PROJECT_NAME_PATTERN_STRING)
                && project.getReward().compareTo(REWARD_MIN_VALUE) > 0
                && project.getDescription().matches(DESCRIPTION_PATTERN_STRING);
    }

    private boolean fieldsAreNotNull(ProjectServiceModel project) {
        return project.getDescription() != null
                && project.getName() != null
                && project.getReward() != null;
    }
}
