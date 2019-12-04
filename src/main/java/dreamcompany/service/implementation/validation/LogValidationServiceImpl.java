package dreamcompany.service.implementation.validation;

import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.service.interfaces.validation.LogValidationService;
import org.springframework.stereotype.Component;

@Component
public class LogValidationServiceImpl implements LogValidationService {

    @Override
    public boolean isValid(LogServiceModel logServiceModel) {
        return logServiceModel != null && fieldsAreNotNull(logServiceModel);
    }

    private boolean fieldsAreNotNull(LogServiceModel logServiceModel) {
        return logServiceModel.getUsername() != null
                && logServiceModel.getCreatedOn() != null
                && logServiceModel.getDescription() != null;
    }
}
