package dreamcompany.web.controller;

import dreamcompany.domain.entity.Status;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.domain.model.view.ProjectHomeViewModel;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.TeamService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MyThread;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController extends BaseController {

    private final UserService userService;

    private final ProjectService projectService;

    private final TeamService teamService;

    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(UserService userService, ProjectService projectService, TeamService teamService, ModelMapper modelMapper) {
        this.userService = userService;
        this.projectService = projectService;
        this.teamService = teamService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index() {

        return view("index");
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(HttpServletRequest request, ModelAndView modelAndView) {

        String loggedUserName = request.getUserPrincipal().getName();

        boolean isLeaderWithAssignedProject = userService.isLeaderWithAssignedProject(loggedUserName);

        modelAndView.addObject("user", loggedUserName);
        modelAndView.addObject("isLeaderWithAssignedProject", isLeaderWithAssignedProject);
        modelAndView.addObject("statuses", Status.values());

        if (isLeaderWithAssignedProject) {

            ProjectServiceModel projectServiceModel = userService.findByUsername(loggedUserName).getTeam().getProject();
            ProjectHomeViewModel viewModel = modelMapper.map(projectServiceModel, ProjectHomeViewModel.class);
            modelAndView.addObject("project", viewModel);

            boolean projectIsCompleted = projectService.projectIsCompleted(projectServiceModel.getId());
            modelAndView.addObject("projectIsCompleted",projectIsCompleted);
        }

        return view("home", modelAndView);
    }
}
