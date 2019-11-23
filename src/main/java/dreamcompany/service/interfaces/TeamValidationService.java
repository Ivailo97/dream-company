package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.TeamServiceModel;

public interface TeamValidationService {

    boolean isValid(TeamServiceModel teamServiceModel);
}
