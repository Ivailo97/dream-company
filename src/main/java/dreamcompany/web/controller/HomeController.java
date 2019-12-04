package dreamcompany.web.controller;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Status;
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
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class HomeController extends BaseController {

    private final UserService userService;

    private final ProjectService projectService;

    private final FriendRequestService friendRequestService;

    private final MappingConverter mappingConverter;

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

        boolean isLeaderWithAssignedProject = userService.isLeaderWithAssignedProject(loggedUserName);

        HttpSession session = request.getSession();
        session.setAttribute("isLeaderWithAssignedProject",isLeaderWithAssignedProject);
        String imageUrl = loggedUser.getImageUrl();

        boolean isLeader = loggedUser.getPosition() == Position.TEAM_LEADER;

        session.setAttribute("userImageUrl", imageUrl == null ? "/img/default-avatar.png" : imageUrl);
        session.setAttribute("isTeamLeader",isLeader);
        modelAndView.addObject("statuses", Status.values());

        if (isLeader){
            session.setAttribute("teamId",loggedUser.getTeam().getId());
        }

        if (isLeaderWithAssignedProject) {
            session.setAttribute("projectId",loggedUser.getTeam().getProject().getId());
            ProjectServiceModel projectServiceModel = loggedUser.getTeam().getProject();
            ProjectHomeViewModel viewModel = mappingConverter.map(projectServiceModel, ProjectHomeViewModel.class);
            modelAndView.addObject("project", viewModel);
            modelAndView.addObject("hasTasks", !projectServiceModel.getTasks().isEmpty());
            boolean projectIsCompleted = projectService.projectIsCompleted(projectServiceModel.getId());
            modelAndView.addObject("projectIsCompleted", projectIsCompleted);
        }

        List<FriendRequestServiceModel> friendRequests = friendRequestService
                .findRequestsForUser(loggedUserName);

        modelAndView.addObject("friendRequests",friendRequests);
        return view("home", modelAndView);
    }
}
