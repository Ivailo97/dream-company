package dreamcompany.web.controller;

import dreamcompany.domain.entity.Status;
import dreamcompany.domain.model.binding.ProjectCreateBindingModel;
import dreamcompany.domain.model.binding.ProjectEditBindingModel;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.validation.project.binding.ProjectCreateValidator;
import dreamcompany.validation.project.binding.ProjectEditValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
@PreAuthorize("isAuthenticated()")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    private final ProjectCreateValidator createValidator;

    private final ProjectEditValidator editValidator;

    private final TaskService taskService;

    private final ModelMapper modelMapper;

    @Autowired
    public ProjectController(ProjectService projectService, ProjectCreateValidator createValidator, ProjectEditValidator editValidator, TaskService taskService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.createValidator = createValidator;
        this.editValidator = editValidator;
        this.taskService = taskService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView create(@ModelAttribute(name = "model") ProjectCreateBindingModel model) {
        return view("/project/create");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "model") ProjectCreateBindingModel model, BindingResult bindingResult) {

        createValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return view("/project/create");
        }

        ProjectServiceModel projectServiceModel = modelMapper.map(model, ProjectServiceModel.class);
        projectService.create(projectServiceModel);

        return redirect("/home");
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {

        ProjectServiceModel projectServiceModel = projectService.findById(id);

        ProjectDetailsViewModel viewModel = modelMapper.map(projectServiceModel, ProjectDetailsViewModel.class);

        viewModel.setTasks(projectServiceModel.getTasks().stream().map(TaskServiceModel::getName).collect(Collectors.toSet()));

        modelAndView.addObject("statuses", Status.values());
        modelAndView.addObject("model", viewModel);

        return view("/project/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {

        ProjectServiceModel projectServiceModel = projectService.findById(id);

        ProjectEditBindingModel projectEditBindingModel = modelMapper.map(projectServiceModel, ProjectEditBindingModel.class);

        modelAndView.addObject("model", projectEditBindingModel);

        return view("/project/edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute(name = "model") ProjectEditBindingModel model,
                                    BindingResult bindingResult) {

        editValidator.validate(model, bindingResult);

        if (bindingResult.hasErrors()) {
            return view("/project/edit");
        }

        ProjectServiceModel projectServiceModel = modelMapper.map(model, ProjectServiceModel.class);
        projectService.edit(id, projectServiceModel);

        return redirect("/projects/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {

        ProjectServiceModel projectServiceModel = projectService.findById(id);

        ProjectDeleteViewModel projectDeleteViewModel = modelMapper.map(projectServiceModel, ProjectDeleteViewModel.class);
        projectDeleteViewModel.setTasks(projectServiceModel.getTasks().stream().map(TaskServiceModel::getName).collect(Collectors.toSet()));

        modelAndView.addObject("model", projectDeleteViewModel);

        return view("/project/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {

        projectService.delete(id);
        return redirect("/projects/all");
    }

    @GetMapping("/manage")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView manage(ModelAndView modelAndView) {

        List<ProjectAllViewModel> models = projectService.findAllByStatus(Status.PENDING.name())
                .stream()
                .map(p -> modelMapper.map(p, ProjectAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("models", models);

        return view("/project/manage", modelAndView);
    }

    @GetMapping("/project-tasks/{id}")
    public ModelAndView projectTasks(@PathVariable String id, ModelAndView modelAndView) {

        List<TaskAssignViewModel> viewModels = taskService.findAllNonAssignedByProjectId(id)
                .stream()
                .map(t -> modelMapper.map(t, TaskAssignViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("tasks", viewModels);

        return view("/project/tasks", modelAndView);
    }

    @PostMapping("/complete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView completeProject(@PathVariable String id) {

        projectService.complete(id);
        return redirect("/home");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<ProjectFetchViewModel> fetch() {

        return projectService.findAll().stream()
                .map(p -> modelMapper.map(p, ProjectFetchViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/fetch/{status}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    @ResponseBody
    public List<ProjectFetchViewModel> fetchByStatus(@PathVariable String status) {

        return projectService.findAllByStatus(status).stream()
                .map(p -> modelMapper.map(p, ProjectFetchViewModel.class))
                .collect(Collectors.toList());
    }
}
