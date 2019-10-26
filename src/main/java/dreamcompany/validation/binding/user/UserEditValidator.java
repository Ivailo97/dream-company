package dreamcompany.validation.binding.user;

import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.binding.UserEditBindingModel;
import dreamcompany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dreamcompany.validation.binding.ValidationConstants.*;


@dreamcompany.validation.binding.annotation.Validator
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
            errors.rejectValue(OLD_PASSWORD_FIELD_NAME, OLD_PASSWORD_IS_MANDATORY, OLD_PASSWORD_IS_MANDATORY);
        }

        if (!encoder.matches(model.getOldPassword(), user.getPassword())) {
            errors.rejectValue(OLD_PASSWORD_FIELD_NAME, WRONG_OLD_PASSWORD, WRONG_OLD_PASSWORD);
        }

        if (model.getPassword() == null || model.getPassword().isEmpty()) {
            errors.rejectValue(PASSWORD_FIELD_NAME, PASSWORD_IS_MANDATORY, PASSWORD_IS_MANDATORY);
        }

        if (model.getConfirmPassword() == null || model.getConfirmPassword().isEmpty()) {
            errors.rejectValue(CONFIRM_PASSWORD_FIELD_NAME, CONFIRM_PASSWORD_IS_MANDATORY, CONFIRM_PASSWORD_IS_MANDATORY);
        }

        if (model.getPassword() != null && !model.getPassword().equals(model.getConfirmPassword())) {
            errors.rejectValue(PASSWORD_FIELD_NAME, PASSWORDS_DO_NOT_MATCH, PASSWORDS_DO_NOT_MATCH);
        }


        if (!user.getEmail().equals(model.getEmail()) && userRepository.findByEmail(model.getEmail()).isPresent()) {
            errors.rejectValue(
                    EMAIL_FIELD_NAME,
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail()),
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail())
            );
        }

        Pattern pattern = Pattern.compile(EMAIL_PATTERN_STRING);
        Matcher matcher = pattern.matcher(model.getEmail());

        if (!matcher.matches()) {
            errors.rejectValue(EMAIL_FIELD_NAME, EMAIL_IS_NOT_VALID, EMAIL_IS_NOT_VALID);
        }

        if (model.getFirstName() == null || model.getFirstName().isEmpty()) {
            errors.rejectValue(FIRST_NAME_FIELD_NAME, FIRST_NAME_IS_MANDATORY, FIRST_NAME_IS_MANDATORY);
        }

        if (model.getLastName() == null || model.getLastName().isEmpty()) {
            errors.rejectValue(LAST_NAME_FIELD_NAME, LAST_NAME_IS_MANDATORY, LAST_NAME_IS_MANDATORY);
        }

        if (model.getFirstName().length() < FIRST_NAME_MIN_LENGTH || model.getFirstName().length() > FIRST_NAME_MAX_LENGTH) {
            errors.rejectValue(FIRST_NAME_FIELD_NAME, FIRST_NAME_LENGTH, FIRST_NAME_LENGTH);
        }

        if (model.getLastName().length() < LAST_NAME_MIN_LENGTH || model.getLastName().length() > LAST_NAME_MAX_LENGTH) {
            errors.rejectValue(LAST_NAME_FIELD_NAME, LAST_NAME_LENGTH, LAST_NAME_LENGTH);
        }
    }
}
