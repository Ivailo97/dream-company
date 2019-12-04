package dreamcompany.web.controller;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.binding.TaskCreateBindingModel;
import dreamcompany.domain.model.binding.TaskEditBindingModel;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.domain.model.view.TaskDeleteViewModel;
import dreamcompany.domain.model.view.TaskDetailsViewModel;
import dreamcompany.domain.model.view.TaskFetchViewModel;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.util.MappingConverter;
import dreamcompany.validation.task.binding.TaskCreateValidator;
import dreamcompany.validation.task.binding.TaskEditValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
@PreAuthorize("isAuthenticated()")
public class TaskController extends BaseController {

    private final TaskService taskService;

    private final TaskCreateValidator createValidator;

    private final TaskEditValidator editValidator;

    private final MappingConverter mappingConverter;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "model") TaskCreateBindingModel model,
                                      BindingResult bindingResult) {
        createValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("/validation/invalid-task-form");
        }

        TaskServiceModel taskServiceModel = mappingConverter.map(model, TaskServiceModel.class);
        taskService.create(taskServiceModel);
        return redirect("/home");
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        TaskDetailsViewModel taskDetailsViewModel = mappingConverter
                .convert(taskService.findById(id),TaskDetailsViewModel.class);
        modelAndView.addObject("model", taskDetailsViewModel);
        return view("/task/details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {
        TaskEditBindingModel taskEditBindingModel = mappingConverter
                .map(taskService.findById(id), TaskEditBindingModel.class);
        modelAndView.addObject("model", taskEditBindingModel);
        return view("/task/edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute(name = "model") TaskEditBindingModel model,
                                    BindingResult bindingResult) {
        editValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("/task/edit");
        }

        TaskServiceModel taskServiceModel = mappingConverter.map(model, TaskServiceModel.class);
        taskService.edit(id, taskServiceModel);
        return redirect("/show");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        TaskDeleteViewModel taskDeleteViewModel = mappingConverter
                .map(taskService.findById(id), TaskDeleteViewModel.class);
        modelAndView.addObject("model", taskDeleteViewModel);
        return view("/task/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        taskService.delete(id);
        return redirect("/show");
    }

    @GetMapping("/loading/{id}")
    public ModelAndView loading(@PathVariable String id, ModelAndView modelAndView) {
        modelAndView.addObject("minutes", taskService.findById(id).getMinutesNeeded());
        return view("task/loading", modelAndView);
    }

    @GetMapping("/fetchPositions")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<String> fetchPositions() {
        return Arrays.stream(Position.values())
                .filter(position -> !position.equals(Position.PROJECT_MANAGER))
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @GetMapping("/fetch/{projectId}/{status}")
    @ResponseBody
    public List<TaskFetchViewModel> fetch(@PathVariable String projectId, @PathVariable String status) {
        return mappingConverter.convertCollection(taskService.findAllByProjectIdAndStatus(projectId, status),TaskFetchViewModel.class);
    }
}
