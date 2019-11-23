package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.OfficeServiceModel;

public interface OfficeValidationService {

    boolean isValid(OfficeServiceModel office);
}
