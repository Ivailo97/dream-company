package dreamcompany.error;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Incorrect old password")
public class WrongOldPasswordException extends BaseException {

    public WrongOldPasswordException(String message) {
        super(401, message);
    }
}
