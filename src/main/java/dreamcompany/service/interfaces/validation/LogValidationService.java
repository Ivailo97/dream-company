package dreamcompany.service.interfaces.validation;

import dreamcompany.domain.model.service.LogServiceModel;

public interface LogValidationService {

    boolean isValid(LogServiceModel logServiceModel);
}
