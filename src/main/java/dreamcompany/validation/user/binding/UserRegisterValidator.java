package dreamcompany.validation.user.binding;

import dreamcompany.domain.model.binding.UserRegisterBindingModel;
import dreamcompany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static dreamcompany.validation.user.UserConstants.*;

@dreamcompany.validation.annotation.Validator
public class UserRegisterValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserRegisterValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegisterBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        UserRegisterBindingModel model = (UserRegisterBindingModel) o;

        if (!model.getUsername().matches(USERNAME_PATTERN_STRING)) {
            errors.rejectValue(USERNAME_FIELD, USERNAME_IS_INVALID, USERNAME_IS_INVALID);
        }

        if (userRepository.findByUsername(model.getUsername()).isPresent()) {
            errors.rejectValue(
                    USERNAME_FIELD,
                    String.format(USERNAME_ALREADY_EXISTS, model.getUsername()),
                    String.format(USERNAME_ALREADY_EXISTS, model.getUsername())
            );
        }

        if (!model.getFirstName().matches(NAME_PATTERN_STRING)) {
            errors.rejectValue(FIRST_NAME_FIELD, FIRST_NAME_IS_INVALID, FIRST_NAME_IS_INVALID);
        }

        if (!model.getLastName().matches(NAME_PATTERN_STRING)) {
            errors.rejectValue(LAST_NAME_FIELD, LAST_NAME_IS_INVALID, LAST_NAME_IS_INVALID);
        }

        if (model.getPassword() == null || model.getPassword().isEmpty()) {
            errors.rejectValue(PASSWORD_FIELD, PASSWORD_IS_MANDATORY, PASSWORD_IS_MANDATORY);
        }

        if (model.getConfirmPassword() == null || model.getConfirmPassword().isEmpty()) {
            errors.rejectValue(CONFIRM_PASSWORD_FIELD, CONFIRM_PASSWORD_IS_MANDATORY, CONFIRM_PASSWORD_IS_MANDATORY);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            errors.rejectValue(PASSWORD_FIELD, PASSWORDS_DO_NOT_MATCH, PASSWORDS_DO_NOT_MATCH);
        }

        if (userRepository.findByEmail(model.getEmail()).isPresent()) {
            errors.rejectValue(
                    EMAIL_FIELD,
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail()),
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail())
            );
        }

        if (!model.getEmail().matches(EMAIL_PATTERN_STRING)) {
            errors.rejectValue(EMAIL_FIELD, EMAIL_IS_NOT_VALID, EMAIL_IS_NOT_VALID);
        }
    }
}
