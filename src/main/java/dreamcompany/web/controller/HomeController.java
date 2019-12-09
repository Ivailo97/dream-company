package dreamcompany.web.controller;

import dreamcompany.domain.model.service.FriendRequestServiceModel;
import dreamcompany.service.interfaces.FriendRequestService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController extends BaseController {

    private final FriendRequestService friendRequestService;

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index() {
        return view("index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(HttpServletRequest request, ModelAndView modelAndView) {

        String loggedUserName = request.getUserPrincipal().getName();
        List<FriendRequestServiceModel> friendRequests = friendRequestService
                .findRequestsForUser(loggedUserName);

        modelAndView.addObject("friendRequests",friendRequests);
        return view("home", modelAndView);
    }
}
