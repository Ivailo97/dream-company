package dreamcompany.web.controller;

import dreamcompany.domain.model.binding.OfficeCreateBindingModel;
import dreamcompany.domain.model.binding.ProjectCreateBindingModel;
import dreamcompany.domain.model.binding.TaskCreateBindingModel;
import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/create")
@PreAuthorize("hasRole('ROLE_MODERATOR')")
public class CreateController extends BaseController {

    @GetMapping("")
    public ModelAndView create(@ModelAttribute(name = "model") Object model) {
        return view("create");
    }

    @GetMapping("/project")
    public ModelAndView createProject(@ModelAttribute(name = "model") ProjectCreateBindingModel model) {
        return view("/fragments/create-project");
    }

    @GetMapping("/task")
    public ModelAndView createTask(@ModelAttribute(name = "model") TaskCreateBindingModel model) {
        return view("/fragments/create-task");
    }

    @GetMapping("/office")
    public ModelAndView createOffice(@ModelAttribute(name = "model") OfficeCreateBindingModel model) {
        return view("/fragments/create-office");
    }

    @GetMapping("/team")
    public ModelAndView createTeam(@ModelAttribute(name = "model") TeamCreateBindingModel model) {
        return view("/fragments/create-team");
    }
}
