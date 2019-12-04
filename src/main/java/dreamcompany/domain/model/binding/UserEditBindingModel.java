package dreamcompany.domain.model.binding;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserEditBindingModel {

    private MultipartFile picture;

    private String username;

    private String firstName;

    private String lastName;

    private String oldPassword;

    private String password;

    private String confirmPassword;

    private String email;
}
