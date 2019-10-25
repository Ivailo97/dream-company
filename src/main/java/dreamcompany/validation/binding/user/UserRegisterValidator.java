package dreamcompany.validation.binding.user;

import dreamcompany.domain.model.binding.UserRegisterBindingModel;
import dreamcompany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static dreamcompany.validation.binding.ValidationConstants.*;

@dreamcompany.validation.binding.annotation.Validator
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

        if (model.getUsername() == null || model.getUsername().isEmpty()) {
            errors.rejectValue("username", USERNAME_IS_MANDATORY, USERNAME_IS_MANDATORY);
        }

        if (userRepository.findByUsername(model.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    String.format(USERNAME_ALREADY_EXISTS, model.getUsername()),
                    String.format(USERNAME_ALREADY_EXISTS, model.getUsername())
            );
        }

        if (model.getUsername().length() < 3 || model.getUsername().length() > 10) {
            errors.rejectValue("username", USERNAME_LENGTH, USERNAME_LENGTH);
        }

        if (model.getFirstName().length() < 3 || model.getFirstName().length() > 10) {
            errors.rejectValue("firstName", FIRST_NAME_LENGTH, FIRST_NAME_LENGTH);
        }

        if (model.getFirstName() == null || model.getFirstName().isEmpty()) {
            errors.rejectValue("firstName", FIRST_NAME_IS_MANDATORY, FIRST_NAME_IS_MANDATORY);
        }

        if (model.getLastName() == null || model.getLastName().isEmpty()) {
            errors.rejectValue("lastName", LAST_NAME_IS_MANDATORY, LAST_NAME_IS_MANDATORY);
        }

        if (model.getLastName().length() < 5 || model.getLastName().length() > 11) {
            errors.rejectValue("lastName", LAST_NAME_LENGTH, LAST_NAME_LENGTH);
        }

        if (model.getPassword() == null || model.getPassword().isEmpty()) {
            errors.rejectValue("password", PASSWORD_IS_MANDATORY, PASSWORD_IS_MANDATORY);
        }

        if (model.getConfirmPassword() == null || model.getConfirmPassword().isEmpty()) {
            errors.rejectValue("confirmPassword", CONFIRM_PASSWORD_IS_MANDATORY, CONFIRM_PASSWORD_IS_MANDATORY);
        }

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            errors.rejectValue("password", PASSWORDS_DO_NOT_MATCH, PASSWORDS_DO_NOT_MATCH);
        }

        if (userRepository.findByEmail(model.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail()),
                    String.format(EMAIL_ALREADY_EXISTS, model.getEmail())
            );
        }

        Pattern pattern = Pattern.compile("^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)");
        Matcher matcher = pattern.matcher(model.getEmail());

        if (!matcher.matches()) {
            errors.rejectValue("email", EMAIL_IS_NOT_VALID, EMAIL_IS_NOT_VALID);
        }
    }
}
