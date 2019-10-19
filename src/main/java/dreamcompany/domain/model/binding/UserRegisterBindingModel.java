package dreamcompany.domain.model.binding;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserRegisterBindingModel {

    private String username;

    private String password;

    private String confirmPassword;

    private String email;

    private String firstName;

    private String lastName;

    public UserRegisterBindingModel() {
    }

    @NotEmpty(message = "Username can't be empty")
    @NotNull(message = "Username can't be null")
    @Length(min = 3, max = 10, message = "Length must be between 3 and 10 symbols")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = "Password can't be empty")
    @NotNull(message = "Password can't be null")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(message = "Confirmed password can't be empty")
    @NotNull(message = "Confirmed password can't be null")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @NotNull(message = "Email can't be null")
    @NotEmpty(message = "Email can't be empty")
    @Email(regexp = "^((\"[\\w-\\s]+\")|([\\w-]+(?:\\.[\\w-]+)*)|(\"[\\w-\\s]+\")([\\w-]+(?:\\.[\\w-]+)*))(@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$)|(@\\[?((25[0-5]\\.|2[0-4][0-9]\\.|1[0-9]{2}\\.|[0-9]{1,2}\\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\\]?$)", message = "Invalid email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @NotEmpty(message = "First name can't be empty")
    @NotNull(message = "First name can't be null")
    @Length(min = 3, max = 10, message = "Length must be between 3 and 10 symbols")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotEmpty(message = "Last name can't be empty")
    @NotNull(message = "Last name can't be null")
    @Length(min = 5, max = 11, message = "Length must be between 5 and 11 symbols")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
