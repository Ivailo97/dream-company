package dreamcompany.error;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cant remove yourself from friends")
public class InvalidOperationException extends BaseException {

    public InvalidOperationException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
