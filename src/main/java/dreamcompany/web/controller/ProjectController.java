package dreamcompany.web.controller;

import dreamcompany.domain.enumeration.Status;
import dreamcompany.domain.model.binding.ProjectCreateBindingModel;
import dreamcompany.domain.model.binding.ProjectEditBindingModel;
import dreamcompany.domain.model.service.ProjectServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MappingConverter;
import dreamcompany.validation.project.binding.ProjectCreateValidator;
import dreamcompany.validation.project.binding.ProjectEditValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/projects")
@PreAuthorize("isAuthenticated()")
public class ProjectController extends BaseController {

    private final ProjectService projectService;

    private final ProjectCreateValidator createValidator;

    private final ProjectEditValidator editValidator;

    private final UserService userService;

    private final TaskService taskService;

    private final MappingConverter mappingConverter;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "model") ProjectCreateBindingModel model,
                                      BindingResult bindingResult) {
        createValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("/validation/invalid-project-form");
        }

        ProjectServiceModel projectServiceModel = mappingConverter.map(model, ProjectServiceModel.class);
        projectService.create(projectServiceModel);
        return redirect("/home");
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView projectStatistics(ModelAndView modelAndView) {
        modelAndView.addObject("statuses", Status.values());
        return view("/project/statistics", modelAndView);
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        ProjectDetailsViewModel viewModel = mappingConverter.convert(projectService.findById(id), ProjectDetailsViewModel.class);
        modelAndView.addObject("statuses", Status.values());
        modelAndView.addObject("model", viewModel);
        return view("/project/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {
        ProjectEditBindingModel model = mappingConverter.map(projectService.findById(id), ProjectEditBindingModel.class);
        modelAndView.addObject("model", model);
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

        projectService.edit(id, mappingConverter.map(model, ProjectServiceModel.class));
        return redirect("/show");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        ProjectDeleteViewModel projectDeleteViewModel = mappingConverter
                .convert(projectService.findById(id), ProjectDeleteViewModel.class);
        modelAndView.addObject("model", projectDeleteViewModel);
        return view("/project/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        projectService.delete(id);
        return redirect("/show");
    }

    @GetMapping("/assigned")
    @PreAuthorize("@userServiceImpl.isLeaderWithAssignedProject(#principal.name)")
    public ModelAndView assignedProject(ModelAndView modelAndView, Principal principal) {

        UserServiceModel loggedUser = userService.findByUsername(principal.getName());

        boolean isLeaderWithAssignedProject = userService.isLeaderWithAssignedProject(loggedUser.getUsername());

        if (isLeaderWithAssignedProject) {
            ProjectServiceModel projectServiceModel = loggedUser.getTeam().getProject();
            ProjectHomeViewModel viewModel = mappingConverter.map(projectServiceModel, ProjectHomeViewModel.class);
            modelAndView.addObject("project", viewModel);
            modelAndView.addObject("hasTasks", !projectServiceModel.getTasks().isEmpty());
            boolean projectIsCompleted = projectService.projectIsCompleted(projectServiceModel.getId());
            modelAndView.addObject("projectIsCompleted", projectIsCompleted);
        }

        return view("/project/assigned", modelAndView);
    }

    @GetMapping("/manage")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView manage(ModelAndView modelAndView) {
        List<ProjectAllViewModel> models = mappingConverter
                .convertCollection(projectService.findAllByStatus(Status.PENDING.name()), ProjectAllViewModel.class);
        modelAndView.addObject("models", models);
        return view("/project/manage", modelAndView);
    }

    @GetMapping("/project-tasks/{id}")
    public ModelAndView projectTasks(@PathVariable String id, ModelAndView modelAndView) {
        List<TaskAssignViewModel> viewModels = mappingConverter
                .convertCollection(taskService.findAllNonAssignedByProjectId(id), TaskAssignViewModel.class);
        modelAndView.addObject("tasks", viewModels);
        return view("/project/tasks", modelAndView);
    }

    @PostMapping("/complete/{id}")
    @PreAuthorize("@userServiceImpl.isLeaderWithAssignedProject(#principal.name)")
    @ResponseBody
    public ResponseEntity<Void> completeProject(@PathVariable String id, Principal principal) {
        projectService.complete(id);
        return new ResponseEntity<>(null, HttpStatus.GONE);
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<ProjectFetchViewModel> fetch() {
        return mappingConverter.convertCollection(projectService.findAllNotCompleted(), ProjectFetchViewModel.class);
    }

    @GetMapping("/fetch/{status}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    @ResponseBody
    public List<ProjectFetchViewModel> fetchByStatus(@PathVariable String status) {
        return mappingConverter.convertCollection(projectService.findAllByStatus(status), ProjectFetchViewModel.class);
    }
}
