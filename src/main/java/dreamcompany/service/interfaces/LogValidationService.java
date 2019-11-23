package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.LogServiceModel;

public interface LogValidationService {

    boolean isValid(LogServiceModel logServiceModel);
}
