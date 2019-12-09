package dreamcompany.web.controller;

import dreamcompany.domain.model.view.OfficeAllViewModel;
import dreamcompany.domain.model.view.ProjectAllViewModel;
import dreamcompany.domain.model.view.TaskAllViewModel;
import dreamcompany.domain.model.view.TeamAllViewModel;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.TeamService;
import dreamcompany.util.MappingConverter;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/show")
@PreAuthorize("hasRole('ROLE_MODERATOR')")
public class ShowController extends BaseController {

    private final TeamService teamService;

    private final ProjectService projectService;

    private final TaskService taskService;

    private final OfficeService officeService;

    private final MappingConverter mappingConverter;

    @GetMapping("")
    public ModelAndView show() {
        return view("show");
    }

    @GetMapping("/teams")
    public ModelAndView allTeams(ModelAndView modelAndView) {
        List<TeamAllViewModel> viewModels = mappingConverter.convertCollection(teamService.findAll(), TeamAllViewModel.class);
        mappingConverter.convertCollection(teamService.findAll(),TeamAllViewModel.class);
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-teams", modelAndView);
    }

    @GetMapping("/projects")
    public ModelAndView allProjects(ModelAndView modelAndView) {
        List<ProjectAllViewModel> viewModels = mappingConverter
                .convertCollection(projectService.findAll(),ProjectAllViewModel.class);
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-projects", modelAndView);
    }

    @GetMapping("/tasks")
    public ModelAndView allTasks(ModelAndView modelAndView) {
        List<TaskAllViewModel> viewModels = mappingConverter
                .convertCollection(taskService.findAll(),TaskAllViewModel.class);
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-tasks", modelAndView);
    }

    @GetMapping("/offices")
    public ModelAndView allOffices(ModelAndView modelAndView) {
        List<OfficeAllViewModel> viewModels = mappingConverter
                .convertCollection(officeService.findAll(),OfficeAllViewModel.class);
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-offices", modelAndView);
    }
}
