package dreamcompany.service.implementation;

import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.service.interfaces.UserValidationService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static dreamcompany.validation.user.UserConstants.*;

@Service
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
