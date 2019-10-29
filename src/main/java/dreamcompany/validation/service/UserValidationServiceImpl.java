package dreamcompany.validation.service;

import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.validation.service.interfaces.UserValidationService;
import org.springframework.stereotype.Component;

import static dreamcompany.validation.binding.ValidationConstants.*;

import java.util.regex.Pattern;

@Component
public class UserValidationServiceImpl implements UserValidationService {

    private static final Pattern REGEX_PATTERN = Pattern.compile(EMAIL_PATTERN_STRING);

    @Override
    public boolean isValid(UserServiceModel userServiceModel) {
        return userServiceModel != null && fieldsAreValid(userServiceModel);
    }

    private boolean fieldsAreValid(UserServiceModel userServiceModel) {
        return fieldsAreNotNull(userServiceModel) && fieldsMeetTheRequirements(userServiceModel);
    }

    private boolean fieldsMeetTheRequirements(UserServiceModel userServiceModel) {
        return REGEX_PATTERN.matcher(userServiceModel.getEmail()).matches()
                && !userServiceModel.getPassword().isEmpty()
                && userServiceModel.getUsername().length() >= USERNAME_MIN_LENGTH
                && userServiceModel.getUsername().length() <= USERNAME_MAX_LENGTH
                && userServiceModel.getFirstName().length() >= FIRST_NAME_MIN_LENGTH
                && userServiceModel.getFirstName().length() <= FIRST_NAME_MAX_LENGTH
                && userServiceModel.getLastName().length() >= LAST_NAME_MIN_LENGTH
                && userServiceModel.getLastName().length() <= LAST_NAME_MAX_LENGTH;
    }

    private boolean fieldsAreNotNull(UserServiceModel userServiceModel) {
        return userServiceModel.getEmail() != null
                && userServiceModel.getFirstName() != null
                && userServiceModel.getLastName() != null
                && userServiceModel.getPassword() != null
                && userServiceModel.getUsername() != null;
    }
}
