package dreamcompany.web.controller;

import dreamcompany.domain.model.view.OfficeAllViewModel;
import dreamcompany.domain.model.view.ProjectAllViewModel;
import dreamcompany.domain.model.view.TaskAllViewModel;
import dreamcompany.domain.model.view.TeamAllViewModel;
import dreamcompany.service.interfaces.OfficeService;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/show")
public class ShowController extends BaseController {

    private final TeamService teamService;

    private final ProjectService projectService;

    private final TaskService taskService;

    private final OfficeService officeService;

    private final ModelMapper modelMapper;

    @Autowired
    public ShowController(TeamService teamService, ProjectService projectService, TaskService taskService, OfficeService officeService, ModelMapper modelMapper) {
        this.teamService = teamService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.officeService = officeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("")
    public ModelAndView show() {
        return view("show");
    }

    @GetMapping("/teams")
    public ModelAndView allTeams(ModelAndView modelAndView) {
        List<TeamAllViewModel> viewModels = teamService.findAll().stream()
                .map(t -> modelMapper.map(t, TeamAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-teams", modelAndView);
    }

    @GetMapping("/projects")
    public ModelAndView allProjects(ModelAndView modelAndView) {
        List<ProjectAllViewModel> viewModels = projectService.findAll().stream()
                .map(t -> modelMapper.map(t, ProjectAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-projects", modelAndView);
    }

    @GetMapping("/tasks")
    public ModelAndView allTasks(ModelAndView modelAndView) {
        List<TaskAllViewModel> viewModels = taskService.findAll().stream()
                .map(t -> modelMapper.map(t, TaskAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-tasks", modelAndView);
    }

    @GetMapping("/offices")
    public ModelAndView allOffices(ModelAndView modelAndView) {
        List<OfficeAllViewModel> viewModels = officeService.findAll().stream()
                .map(t -> modelMapper.map(t, OfficeAllViewModel.class)).collect(Collectors.toList());
        modelAndView.addObject("models", viewModels);
        return view("fragments/all-offices", modelAndView);
    }
}
