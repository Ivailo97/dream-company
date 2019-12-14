package dreamcompany.web.controller;

import dreamcompany.domain.enumeration.Position;
import dreamcompany.domain.model.binding.UserEditBindingModel;
import dreamcompany.domain.model.binding.UserRegisterBindingModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.FriendRequestService;
import dreamcompany.service.interfaces.TaskService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MappingConverter;
import dreamcompany.validation.user.binding.UserEditValidator;
import dreamcompany.validation.user.binding.UserRegisterValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;

    private final TaskService taskService;

    private final UserRegisterValidator registerValidator;

    private final FriendRequestService friendRequestService;

    private final UserEditValidator editValidator;

    private final MappingConverter converter;

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute("model") UserRegisterBindingModel modelAndView) {
        return view("register");
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute(name = "model") UserRegisterBindingModel user,
                                        BindingResult bindingResult) throws RoleNotFoundException {
        registerValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("register");
        }

        UserServiceModel userServiceModel = converter.map(user, UserServiceModel.class);
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
        List<UserAllViewModel> users = converter.convertCollection(userService.findAll(), UserAllViewModel.class);
        modelAndView.addObject("users", users);
        return view("employee/all", modelAndView);
    }

    @PostMapping("/set-admin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setAdminRole(@PathVariable(name = "id") String id, Principal principal) throws RoleNotFoundException {
        userService.changeRoles(id, "admin", principal.getName());
        return redirect("/users/all");
    }

    @PostMapping("/set-moderator/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setModeratorRole(@PathVariable(name = "id") String id, Principal principal) throws RoleNotFoundException {
        userService.changeRoles(id, "moderator", principal.getName());
        return redirect("/users/all");
    }

    @PostMapping("/set-user/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView setUserRole(@PathVariable(name = "id") String id, Principal principal) throws RoleNotFoundException {
        userService.changeRoles(id, "user", principal.getName());
        return redirect("/users/all");
    }

    @GetMapping("/profile/{username}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView profile(@PathVariable String username, ModelAndView modelAndView, Principal principal) {
        UserServiceModel userServiceModel = userService.findByUsername(username);
        UserProfileViewModel userProfileViewModel = converter.convert(userServiceModel, UserProfileViewModel.class);
        modelAndView.addObject("viewModel", userProfileViewModel);
        modelAndView.addObject("canRemoveFriend", userService.canRemoveFriend(principal.getName(), username));
        modelAndView.addObject("canSendFriendRequest", friendRequestService.canSendFriendRequest(username, principal.getName()));
        boolean canAccept = userService.canAcceptRequest(username, principal.getName());
        modelAndView.addObject("canAcceptRequest", canAccept);
        if (canAccept){
            modelAndView.addObject("requestId",friendRequestService.findAllBySenderAndReceiver(username,principal.getName()).get(0).getId());
        }
        return view("/employee/profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView edit(Principal principal, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = userService.findByUsername(principal.getName());
        UserEditBindingModel userEditBindingModel = converter.map(userServiceModel, UserEditBindingModel.class);
        modelAndView.addObject("editModel", userEditBindingModel);
        return view("employee/edit", modelAndView);
    }

    @PatchMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editConfirm(@Valid @ModelAttribute(name = "editModel") UserEditBindingModel model,
                                    BindingResult bindingResult) throws IOException {
        editValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("employee/edit");
        }

        UserServiceModel userServiceModel = converter.convert(model, UserServiceModel.class);
        userService.edit(userServiceModel, model.getOldPassword());
        return redirect("/users/profile/" + userServiceModel.getUsername());
    }

    @GetMapping("/assign-task/{id}")
    @PreAuthorize("@userServiceImpl.isLeaderWithAssignedProject(#principal.name)")
    public ModelAndView assignTask(@PathVariable String id, ModelAndView modelAndView,Principal principal) {
        modelAndView.addObject("chosenTaskId", id);
        String teamId = taskService.findTeamId(id);
        Position requiredPosition = taskService.findRequiredPosition(id);
        List<UserServiceModel> userServiceModels = userService.findAllInTeamWithPosition(teamId, requiredPosition);
        List<UserAssignTaskViewModel> viewModels = converter.convertCollection(userServiceModels, UserAssignTaskViewModel.class);
        modelAndView.addObject("employees", viewModels);
        return view("employee/choose-employee", modelAndView);
    }

    @PostMapping("/assign-task/{taskId}/{userId}")
    public ModelAndView assignProjectConfirm(@PathVariable String taskId, @PathVariable String userId) {
        userService.assignTask(userId, taskId);
        return redirect("/home");
    }

    @GetMapping("/my-tasks")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView myTasks(Principal principal, ModelAndView modelAndView) {
        UserServiceModel loggedUser = userService.findByUsername(principal.getName());
        List<TaskAssignedViewModel> viewModels = converter
                .convertCollection(taskService.findNotFinishedAssignedToUser(loggedUser.getId()), TaskAssignedViewModel.class);
        modelAndView.addObject("tasks", viewModels);
        return view("/employee/assigned-tasks", modelAndView);
    }

    @GetMapping("/promote")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView promotion(ModelAndView modelAndView) {
        List<UserPositionChangeViewModel> viewModels = converter
                .convertCollection(userService.findAllForPromotion(), UserPositionChangeViewModel.class);
        modelAndView.addObject("models", viewModels);
        return view("/employee/for-promotion", modelAndView);
    }

    @PostMapping("/promote/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView promotionConfirm(@PathVariable String id, Principal principal) {
        userService.promote(id, principal.getName());
        return redirect("/users/promote");
    }

    @GetMapping("/demote")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView demotion(ModelAndView modelAndView) {
        List<UserPositionChangeViewModel> viewModels = converter
                .convertCollection(userService.findAllForDemotion(), UserPositionChangeViewModel.class);
        modelAndView.addObject("models", viewModels);
        return view("/employee/for-demotion", modelAndView);
    }

    @PostMapping("/demote/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView demotionConfirm(@PathVariable String id, Principal principal) {
        userService.demote(id, principal.getName());
        return redirect("/users/demote");
    }

    @PostMapping("/complete-task/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView completeTask(@PathVariable String id, Principal principal) {
        UserServiceModel loggedUser = userService.findByUsername(principal.getName());
        userService.completeTask(loggedUser.getId(), id);
        return redirect("/tasks/loading/" + id);
    }

    @PostMapping("/remove-friend")
    @ResponseBody
    public ResponseEntity<Void> removeFriend(@RequestBody String friendUsername, Principal principal) {
        userService.removeFriend(principal.getName(), friendUsername);
        return new ResponseEntity<>(null, HttpStatus.GONE);
    }

    @GetMapping("/fetchNonLeaders")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @ResponseBody
    public List<UserFetchViewModel> fetchAvailableNonLeaders() {
        return converter.convertCollection(userService.findAllNonLeadersWithoutTeam(), UserFetchViewModel.class);
    }
}
