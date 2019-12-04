package dreamcompany.service.interfaces.validation;

import dreamcompany.domain.model.service.OfficeServiceModel;

public interface OfficeValidationService {

    boolean isValid(OfficeServiceModel office);
}
