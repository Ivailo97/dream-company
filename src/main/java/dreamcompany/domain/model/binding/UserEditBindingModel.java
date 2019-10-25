package dreamcompany.domain.model.binding;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserEditBindingModel {

    private MultipartFile picture;

    private String username;

    private String firstName;

    private String lastName;

    private String oldPassword;

    private String password;

    private String confirmPassword;

    private String email;

    public UserEditBindingModel() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    @NotEmpty(message = "Field can't be empty")
    @NotNull(message = "Field can't be null")
    public String getFirstName() {
        return firstName;
    }

    @NotEmpty(message = "Field can't be empty")
    @NotNull(message = "Field can't be null")
    public String getLastName() {
        return lastName;
    }

    @NotEmpty(message = "Field can't be empty")
    @NotNull(message = "Field can't be null")
    public String getOldPassword() {
        return oldPassword;
    }

    @NotEmpty(message = "Field can't be empty")
    @NotNull(message = "Field can't be null")
    public String getPassword() {
        return password;
    }


    @NotEmpty(message = "Field can't be empty")
    @NotNull(message = "Field can't be null")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public MultipartFile getPicture() {
        return picture;
    }
}
