package dreamcompany.error.duplicates;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate team name")
public class TeamNameAlreadyExistException extends BaseException {

    public TeamNameAlreadyExistException(String message) {
        super(409, message);
    }
}
