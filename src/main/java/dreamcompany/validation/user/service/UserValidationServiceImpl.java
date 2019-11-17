package dreamcompany.validation.user.service;

import dreamcompany.domain.model.service.UserServiceModel;
import org.springframework.stereotype.Component;

import static dreamcompany.validation.user.UserConstants.*;

@Component
public class UserValidationServiceImpl implements UserValidationService {

    @Override
    public boolean isValid(UserServiceModel userServiceModel) {
        return userServiceModel != null && fieldsAreValid(userServiceModel);
    }

    private boolean fieldsAreValid(UserServiceModel userServiceModel) {
        return fieldsAreNotNull(userServiceModel) && fieldsMeetTheRequirements(userServiceModel);
    }

    private boolean fieldsMeetTheRequirements(UserServiceModel userServiceModel) {
        return userServiceModel.getEmail().matches(EMAIL_PATTERN_STRING)
                && !userServiceModel.getPassword().isEmpty()
                && userServiceModel.getUsername().matches(USERNAME_PATTERN_STRING)
                && userServiceModel.getFirstName().matches(NAME_PATTERN_STRING)
                && userServiceModel.getLastName().matches(NAME_PATTERN_STRING);
    }

    private boolean fieldsAreNotNull(UserServiceModel userServiceModel) {
        return userServiceModel.getEmail() != null
                && userServiceModel.getFirstName() != null
                && userServiceModel.getLastName() != null
                && userServiceModel.getPassword() != null
                && userServiceModel.getUsername() != null;
    }
}
