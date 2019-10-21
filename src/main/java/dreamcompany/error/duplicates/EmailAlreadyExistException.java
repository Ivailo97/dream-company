package dreamcompany.error.duplicates;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate email")
public class EmailAlreadyExistException extends BaseException {

    public EmailAlreadyExistException(String message) {
        super(409, message);
    }
}
