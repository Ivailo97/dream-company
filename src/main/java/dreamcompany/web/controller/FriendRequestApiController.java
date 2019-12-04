package dreamcompany.web.controller;

import dreamcompany.domain.model.rest.FriendRequestRestModel;
import dreamcompany.service.interfaces.FriendRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requests")
@PreAuthorize("isAuthenticated()")
@AllArgsConstructor
public class FriendRequestApiController extends BaseController {

    private FriendRequestService friendRequestService;

    @PostMapping("/send")
    public ResponseEntity<Void> send(@RequestBody FriendRequestRestModel requestModel) {
        friendRequestService.send(requestModel);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> accept(@RequestBody String id) {
        friendRequestService.accept(id);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }

    @PostMapping("/decline")
    public ResponseEntity<Void> decline(@RequestBody String id) {
        friendRequestService.decline(id);
        return new ResponseEntity<>(null, HttpStatus.ACCEPTED);
    }
}
