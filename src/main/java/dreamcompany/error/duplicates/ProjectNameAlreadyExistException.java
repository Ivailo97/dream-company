package dreamcompany.error.duplicates;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate project name")
public class ProjectNameAlreadyExistException extends BaseException {

    public ProjectNameAlreadyExistException(String message) {
        super(409,message);
    }
}
