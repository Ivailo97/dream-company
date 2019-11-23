package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.service.interfaces.TeamValidationService;
import org.springframework.stereotype.Service;

import static dreamcompany.validation.team.TeamConstants.*;

@Service
public class TeamValidationServiceImpl implements TeamValidationService {

    @Override
    public boolean isValid(TeamServiceModel teamServiceModel) {
        return teamServiceModel != null && fieldsAreValid(teamServiceModel);
    }

    private boolean fieldsAreValid(TeamServiceModel teamServiceModel) {
        return fieldsAreNotNull(teamServiceModel) && fieldsMeetTheRequirements(teamServiceModel);
    }

    private boolean fieldsMeetTheRequirements(TeamServiceModel teamServiceModel) {
        return teamServiceModel.getName().matches(NAME_PATTERN)
                && teamServiceModel.getEmployees().size() >= EMPLOYEES_MIN_COUNT;
    }

    private boolean fieldsAreNotNull(TeamServiceModel teamServiceModel) {
        return teamServiceModel.getLogoUrl() != null
                && teamServiceModel.getLogoId() != null
                &&teamServiceModel.getOffice() != null
                && teamServiceModel.getEmployees() != null
                && teamServiceModel.getName() != null;
    }
}
