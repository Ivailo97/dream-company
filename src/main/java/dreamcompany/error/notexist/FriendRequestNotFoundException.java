package dreamcompany.error.notexist;

import dreamcompany.error.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid friend request id")
public class FriendRequestNotFoundException extends BaseException {

    public FriendRequestNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
