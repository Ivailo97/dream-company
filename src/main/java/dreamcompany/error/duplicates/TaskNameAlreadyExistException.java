package dreamcompany.error.duplicates;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate task name")
public class TaskNameAlreadyExistException extends BaseException {

    public TaskNameAlreadyExistException(String message) {
        super(409, message);
    }
}
