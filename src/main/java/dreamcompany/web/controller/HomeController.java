package dreamcompany.web.controller;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.service.FriendRequestServiceModel;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.domain.model.view.ProjectHomeViewModel;
import dreamcompany.service.interfaces.FriendRequestService;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MappingConverter;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController extends BaseController {

    private final UserService userService;

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

        UserServiceModel loggedUser = userService.findByUsername(loggedUserName);

        HttpSession session = request.getSession();
        String imageUrl = loggedUser.getImageUrl();

        // todo make it in interceptor
        session.setAttribute("userImageUrl", imageUrl == null ? "/img/default-avatar.png" : imageUrl);

        List<FriendRequestServiceModel> friendRequests = friendRequestService
                .findRequestsForUser(loggedUserName);

        modelAndView.addObject("friendRequests",friendRequests);
        return view("home", modelAndView);
    }
}
