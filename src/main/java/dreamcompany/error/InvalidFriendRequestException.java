package dreamcompany.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Cant send friend request to yourself")
public class InvalidFriendRequestException extends BaseException {

    public InvalidFriendRequestException(String message) {
        super(HttpStatus.BAD_REQUEST.value(), message);
    }
}
