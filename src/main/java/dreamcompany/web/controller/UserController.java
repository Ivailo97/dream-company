package dreamcompany.web.controller;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.binding.UserEditBindingModel;
import dreamcompany.domain.model.binding.UserRegisterBindingModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    private final TaskService taskService;

    private final CloudinaryService cloudinaryService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, TaskService taskService, CloudinaryService cloudinaryService, ModelMapper modelMapper) {
        this.userService = userService;
        this.taskService = taskService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute("model") UserRegisterBindingModel modelAndView) {
        return view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute(name = "model") UserRegisterBindingModel user, BindingResult bindingResult) throws RoleNotFoundException {

        if (bindingResult.hasErrors() || !user.getPassword().equals(user.getConfirmPassword())) {
            return view("register");
        }

        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);

        userService.register(userServiceModel);

        return redirect("/users/login");
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login() {
        return view("login");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN') && isAuthenticated()")
    public ModelAndView all(ModelAndView modelAndView) {

        List<UserAllViewModel> users = userService.findAll().stream()
                .map(u -> modelMapper.map(u, UserAllViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("users", users);

        return view("user-all", modelAndView);
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdminRole(@PathVariable(name = "id") String id) throws RoleNotFoundException {
        userService.changeRoles(id, "admin");
        return redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModeratorRole(@PathVariable(name = "id") String id) throws RoleNotFoundException {
        userService.changeRoles(id, "moderator");
        return redirect("/users/all");
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUserRole(@PathVariable(name = "id") String id) throws RoleNotFoundException {
        userService.changeRoles(id, "user");
        return redirect("/users/all");
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView) {

        UserServiceModel userServiceModel = userService.findByUsername(principal.getName());
        UserProfileViewModel userProfileViewModel = modelMapper.map(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("model", userProfileViewModel);

        return view("/employee/profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView edit(Principal principal, ModelAndView modelAndView) {

        UserServiceModel userServiceModel = this.userService.findByUsername(principal.getName());
        UserEditBindingModel userEditBindingModel = this.modelMapper.map(userServiceModel, UserEditBindingModel.class);

        modelAndView.addObject("editModel", userEditBindingModel);
        return view("user-edit", modelAndView);
    }

    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editConfirm(@Valid @ModelAttribute(name = "editModel") UserEditBindingModel model, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors() || !model.getPassword().equals(model.getConfirmPassword())) {
            return view("user-edit");
        }

        UserServiceModel userServiceModel = this.modelMapper.map(model, UserServiceModel.class);

        if (!model.getPicture().isEmpty()) {
            String[] uploadInfo = cloudinaryService.uploadImage(model.getPicture());
            userServiceModel.setImageUrl(uploadInfo[0]);
            userServiceModel.setImageId(uploadInfo[1]);
        }

        this.userService.edit(userServiceModel, model.getOldPassword());
        return redirect("/users/profile");
    }


    //TODO make it so that every TEAM_LEADER IS AN ADMIN TOO

    @GetMapping("/assign-task/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView assignTask(@PathVariable String id, ModelAndView modelAndView) {

        modelAndView.addObject("chosenTaskId", id);

        String teamId = taskService.findTeamId(id);
        Position requiredPosition = taskService.findRequiredPosition(id);

        List<UserServiceModel> userServiceModels = userService.findAllInTeamWithPosition(teamId, requiredPosition);

        List<UserAssignTaskViewModel> viewModels = new ArrayList<>();

        userServiceModels.forEach(u -> {
            UserAssignTaskViewModel viewModel = modelMapper.map(u, UserAssignTaskViewModel.class);
            viewModel.setFullName(String.format("%s %s", u.getFirstName(), u.getLastName()));
            viewModels.add(viewModel);
        });

        modelAndView.addObject("employees", viewModels);

        return view("choose-employee", modelAndView);
    }

    @PostMapping("/assign-task/{taskId}/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView assignProjectConfirm(@PathVariable String taskId, @PathVariable String userId) {

        userService.assignTask(userId, taskId);

        return redirect("/home");
    }

    @GetMapping("/my-tasks")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView myTasks(Principal principal, ModelAndView modelAndView) {

        UserServiceModel loggedUser = userService.findByUsername(principal.getName());

        List<TaskAssignedViewModel> viewModels = taskService.findNotFinishedAssignedToUser(loggedUser.getId())
                .stream()
                .map(t -> modelMapper.map(t, TaskAssignedViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("tasks", viewModels);

        return view("/employee/assigned-tasks", modelAndView);
    }

    @GetMapping("/promote")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView promotion(ModelAndView modelAndView) {

        List<UserPositionChangeViewModel> viewModels = userService.findAllForPromotion()
                .stream()
                .map(u -> modelMapper.map(u, UserPositionChangeViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("models", viewModels);

        return view("/employee/candidates-for-promotion", modelAndView);
    }

    @PostMapping("/promote/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView promotionConfirm(@PathVariable String id) {

        userService.promote(id);
        return redirect("/users/promote");
    }

    @GetMapping("/demote")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView demotion(ModelAndView modelAndView) {

        List<UserPositionChangeViewModel> viewModels = userService.findAllForDemotion()
                .stream()
                .map(u -> modelMapper.map(u, UserPositionChangeViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("models", viewModels);

        return view("/employee/candidates-for-demotion", modelAndView);
    }

    @PostMapping("/demote/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView demotionConfirm(@PathVariable String id) {

        userService.demote(id);
        return redirect("/users/demote");
    }

    @PostMapping("/complete-task/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView completeTask(@PathVariable String id, Principal principal) {

        UserServiceModel loggedUser = userService.findByUsername(principal.getName());

        userService.completeTask(loggedUser.getId(), id);

        return redirect("/tasks/loading/" + id);
    }

    @GetMapping("/fetchNonLeaders")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<UserFetchViewModel> fetchAvailableNonLeaders() {

        return userService.findAllNonLeadersWithoutTeam().stream()
                .map(u -> {
                    UserFetchViewModel model = modelMapper.map(u, UserFetchViewModel.class);
                    model.setFullName(String.format("%s %s", u.getFirstName(), u.getLastName()));
                    return model;
                })
                .collect(Collectors.toList());
    }
}
