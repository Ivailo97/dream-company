package dreamcompany.validation.log.service;

import dreamcompany.domain.model.service.LogServiceModel;
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
