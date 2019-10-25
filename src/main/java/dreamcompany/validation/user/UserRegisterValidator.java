package dreamcompany.validation.user;

import dreamcompany.domain.model.binding.UserRegisterBindingModel;
import dreamcompany.repository.UserRepository;
import dreamcompany.validation.ValidationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        UserRegisterBindingModel userRegisterBindingModel = (UserRegisterBindingModel) o;

        if (userRepository.findByUsername(userRegisterBindingModel.getUsername()).isPresent()) {
            errors.rejectValue(
                    "username",
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername()),
                    String.format(ValidationConstants.USERNAME_ALREADY_EXISTS, userRegisterBindingModel.getUsername())
            );
        }

        if (userRegisterBindingModel.getUsername().length() < 3 || userRegisterBindingModel.getUsername().length() > 10) {
            errors.rejectValue(
                    "username",
                    ValidationConstants.USERNAME_LENGTH,
                    ValidationConstants.USERNAME_LENGTH
            );
        }

        if (userRegisterBindingModel.getFirstName().length() < 3 || userRegisterBindingModel.getFirstName().length() > 10) {
            errors.rejectValue(
                    "firstName",
                    ValidationConstants.FIRST_NAME_LENGTH,
                    ValidationConstants.FIRST_NAME_LENGTH
            );
        }

        if (userRegisterBindingModel.getLastName().length() < 5 || userRegisterBindingModel.getLastName().length() > 11) {
            errors.rejectValue(
                    "lastName",
                    ValidationConstants.LAST_NAME_LENGTH,
                    ValidationConstants.LAST_NAME_LENGTH
            );
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
            );
        }

        if (userRepository.findByEmail(userRegisterBindingModel.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail()),
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userRegisterBindingModel.getEmail())
            );
        }

        Pattern pattern = Pattern.compile("^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)");
        Matcher matcher = pattern.matcher(userRegisterBindingModel.getEmail());

        if (!matcher.matches()) {
            errors.rejectValue(
                    "email",
                    ValidationConstants.EMAIL_IS_NOT_VALID,
                    ValidationConstants.EMAIL_IS_NOT_VALID
            );
        }
    }
}
