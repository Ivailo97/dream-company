package dreamcompany.error.duplicates;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Duplicate address")
public class OfficeAddressAlreadyExists extends BaseException {

    public OfficeAddressAlreadyExists(String message) {
        super(HttpStatus.CONFLICT.value(), message);
    }
}
