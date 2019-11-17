package dreamcompany.validation.user.binding;

import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.binding.UserEditBindingModel;
import dreamcompany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static dreamcompany.validation.user.UserConstants.*;


@dreamcompany.validation.annotation.Validator
public class UserEditValidator implements Validator {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserEditValidator(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserEditBindingModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        UserEditBindingModel model = (UserEditBindingModel) o;

        User user = userRepository.findByUsername(model.getUsername()).orElse(null);

        if (model.getOldPassword() == null || model.getOldPassword().isEmpty()) {
            errors.rejectValue(OLD_PASSWORD_FIELD, OLD_PASSWORD_IS_MANDATORY, OLD_PASSWORD_IS_MANDATORY);
        }

        if (!encoder.matches(model.getOldPassword(), user.getPassword())) {
            errors.rejectValue(OLD_PASSWORD_FIELD, WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
        }

        if (model.getPassword() == null || model.getPassword().isEmpty()) {
            errors.rejectValue(PASSWORD_FIELD, PASSWORD_IS_MANDATORY, PASSWORD_IS_MANDATORY);
        }

        if (model.getConfirmPassword() == null || model.getConfirmPassword().isEmpty()) {
            errors.rejectValue(CONFIRM_PASSWORD_FIELD, CONFIRM_PASSWORD_IS_MANDATORY, CONFIRM_PASSWORD_IS_MANDATORY);
        }

        if (model.getPassword() != null && !model.getPassword().equals(model.getConfirmPassword())) {
            errors.rejectValue(PASSWORD_FIELD, PASSWORDS_DO_NOT_MATCH, PASSWORDS_DO_NOT_MATCH);
        }

        if (!user.getEmail().equals(model.getEmail()) && userRepository.findByEmail(model.getEmail()).isPresent()) {
            errors.rejectValue(
                    EMAIL_FIELD,
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail()),
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail())
            );
        }

        if (!model.getEmail().matches(EMAIL_PATTERN_STRING)) {
            errors.rejectValue(EMAIL_FIELD, EMAIL_IS_NOT_VALID, EMAIL_IS_NOT_VALID);
        }

        if (!model.getFirstName().matches(NAME_PATTERN_STRING)) {
            errors.rejectValue(FIRST_NAME_FIELD, FIRST_NAME_IS_INVALID, FIRST_NAME_IS_INVALID);
        }

        if (!model.getLastName().matches(NAME_PATTERN_STRING) ) {
            errors.rejectValue(LAST_NAME_FIELD, LAST_NAME_IS_INVALID, LAST_NAME_IS_INVALID);
        }
    }
}
