package dreamcompany.error.invalidservicemodels;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid task service model")
public class InvalidTaskServiceModelException extends BaseException {

    public InvalidTaskServiceModelException( String message) {
        super(HttpStatus.NOT_ACCEPTABLE.value(), message);
    }
}
