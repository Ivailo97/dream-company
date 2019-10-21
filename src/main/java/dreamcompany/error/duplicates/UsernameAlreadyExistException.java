package dreamcompany.error.duplicates;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate username")
public class UsernameAlreadyExistException extends BaseException {

    public UsernameAlreadyExistException(String message) {
        super(409,message);
    }
}
