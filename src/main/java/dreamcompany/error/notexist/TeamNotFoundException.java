package dreamcompany.error.notexist;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid team id")
public class TeamNotFoundException extends BaseException {

    public TeamNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
