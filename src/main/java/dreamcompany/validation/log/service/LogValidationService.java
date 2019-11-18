package dreamcompany.validation.log.service;

import dreamcompany.domain.model.service.LogServiceModel;

public interface LogValidationService {

    boolean isValid(LogServiceModel logServiceModel);
}
