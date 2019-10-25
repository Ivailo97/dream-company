package dreamcompany.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Incorrect old password")
public class WrongOldPasswordException extends BaseException {

    public WrongOldPasswordException(String message) {
        super(HttpStatus.UNAUTHORIZED.value(), message);
    }
}
