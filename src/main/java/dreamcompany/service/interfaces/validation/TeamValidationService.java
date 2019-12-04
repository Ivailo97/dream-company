package dreamcompany.service.interfaces.validation;

import dreamcompany.domain.model.service.TeamServiceModel;

public interface TeamValidationService {

    boolean isValid(TeamServiceModel teamServiceModel);
}
