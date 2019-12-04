package dreamcompany.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

    private int status;

    protected BaseException(int status, String message) {
        super(message);
        this.setStatus(status);
    }
}
