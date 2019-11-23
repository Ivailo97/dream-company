package dreamcompany.error.invalidservicemodels;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE, reason = "Invalid office service model")
public class InvalidOfficeServiceModelException extends BaseException {

    public InvalidOfficeServiceModelException(String message) {
        super(HttpStatus.NOT_ACCEPTABLE.value(), message);
    }
}
