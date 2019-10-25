package dreamcompany.validation.user;

import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.binding.UserEditBindingModel;
import dreamcompany.repository.UserRepository;
import dreamcompany.validation.ValidationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        UserEditBindingModel userEditBindingModel = (UserEditBindingModel) o;

        User user = this.userRepository.findByUsername(userEditBindingModel.getUsername()).orElse(null);

        if (!this.encoder.matches(userEditBindingModel.getOldPassword(), user.getPassword())) {
            errors.rejectValue(
                    "oldPassword",
                    ValidationConstants.WRONG_OLD_PASSWORD,
                    ValidationConstants.WRONG_OLD_PASSWORD
            );
        }

        if (userEditBindingModel.getPassword() != null && !userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
            errors.rejectValue(
                    "password",
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH,
                    ValidationConstants.PASSWORDS_DO_NOT_MATCH
            );
        }


        if (!user.getEmail().equals(userEditBindingModel.getEmail()) && this.userRepository.findByEmail(userEditBindingModel.getEmail()).isPresent()) {
            errors.rejectValue(
                    "email",
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail()),
                    String.format(ValidationConstants.EMAIL_ALREADY_EXISTS, userEditBindingModel.getEmail())
            );
        }

        Pattern pattern = Pattern.compile("^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)");
        Matcher matcher = pattern.matcher(userEditBindingModel.getEmail());

        if (!matcher.matches()) {
            errors.rejectValue(
                    "email",
                    ValidationConstants.EMAIL_IS_NOT_VALID,
                    ValidationConstants.EMAIL_IS_NOT_VALID
            );
        }

        if (userEditBindingModel.getFirstName().length() < 3 || userEditBindingModel.getFirstName().length() > 10) {
            errors.rejectValue(
                    "firstName",
                    ValidationConstants.FIRST_NAME_LENGTH,
                    ValidationConstants.FIRST_NAME_LENGTH
            );
        }

        if (userEditBindingModel.getLastName().length() < 5 || userEditBindingModel.getLastName().length() > 11) {
            errors.rejectValue(
                    "lastName",
                    ValidationConstants.LAST_NAME_LENGTH,
                    ValidationConstants.LAST_NAME_LENGTH
            );
        }
    }
}
