package dreamcompany.error.invalidservicemodels;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid user service model")
public class InvalidUserServiceModelException extends BaseException {

    public InvalidUserServiceModelException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE.value(), message);
    }
}
