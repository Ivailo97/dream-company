package dreamcompany.web.controller;

import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import dreamcompany.domain.model.binding.TeamEditBindingModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.TeamService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MappingConverter;
import dreamcompany.util.ScheduledTask;
import dreamcompany.validation.team.binding.TeamCreateValidator;
import dreamcompany.validation.team.binding.TeamEditValidator;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/teams")
@PreAuthorize("isAuthenticated()")
public class TeamController extends BaseController {

    private final TeamService teamService;

    private final UserService userService;

    private final TeamCreateValidator createValidator;

    private final TeamEditValidator editValidator;

    private final ScheduledTask scheduledTask;

    private final MappingConverter mappingConverter;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "model") TeamCreateBindingModel model,
                                      Principal principal,
                                      BindingResult bindingResult) {
        createValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("/validation/invalid-team-form");
        }

        TeamServiceModel teamServiceModel = mappingConverter.convert(model,TeamServiceModel.class);
        teamService.create(teamServiceModel,principal.getName());
        scheduledTask.payTaxesEveryTenMinutes();
        return redirect("/home");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {
        TeamServiceModel teamServiceModel = teamService.findById(id);
        TeamEditBindingModel teamEditBindingModel = mappingConverter.map(teamServiceModel, TeamEditBindingModel.class);
        teamEditBindingModel.setOffice(teamServiceModel.getOffice().getId());
        modelAndView.addObject("model", teamEditBindingModel);
        return view("/team/edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute(name = "model") TeamEditBindingModel model,
                                    Principal principal,
                                    BindingResult bindingResult) throws IOException {
        editValidator.validate(model, bindingResult);
        if (bindingResult.hasErrors()) {
            return view("/team/edit");
        }

        TeamServiceModel teamServiceModel = mappingConverter.convert(model,TeamServiceModel.class);
        teamService.edit(id, teamServiceModel,principal.getName());
        return redirect("/show");
    }

    @GetMapping("/add-employee/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addEmployee(@PathVariable String id, ModelAndView modelAndView) {
        List<UserAddRemoveFromTeamViewModel> viewModels = mappingConverter
                .convertCollection(userService.findAllWithoutTeam(),UserAddRemoveFromTeamViewModel.class);
        modelAndView.addObject("teamId", id);
        modelAndView.addObject("models", viewModels);
        return view("team/add-employee", modelAndView);
    }

    @PostMapping("/add-employee/{teamId}/{userId}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addEmployeeConfirm(@PathVariable String teamId, @PathVariable String userId,Principal principal) {
        teamService.addEmployeeToTeam(teamId, userId,principal.getName());
        return redirect("/show");
    }

    @GetMapping("/remove-employee/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView removeEmployee(@PathVariable String id, ModelAndView modelAndView) {
        List<UserAddRemoveFromTeamViewModel> viewModels = mappingConverter
                .convertCollection(userService.findAllInTeam(id),UserAddRemoveFromTeamViewModel.class);
        modelAndView.addObject("teamId", id);
        modelAndView.addObject("models", viewModels);
        return view("team/remove-employee", modelAndView);
    }

    @PostMapping("/remove-employee/{teamId}/{userId}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView removeEmployeeConfirm(@PathVariable String teamId, @PathVariable String userId,Principal principal) {
        teamService.removeEmployeeFromTeam(teamId, userId,principal.getName());
        return redirect("/show");
    }

    @GetMapping("/details/{id}")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        TeamServiceModel teamServiceModel = teamService.findById(id);
        TeamDetailsViewModel viewModel = mappingConverter.convert(teamServiceModel,TeamDetailsViewModel.class);
        modelAndView.addObject("model", viewModel);
        return view("/team/details", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView){
        TeamServiceModel teamServiceModel = teamService.findById(id);
        TeamDeleteViewModel teamDeleteViewModel = mappingConverter
                .convert(teamServiceModel,TeamDeleteViewModel.class);
        modelAndView.addObject("model", teamDeleteViewModel);
        return view("/team/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id,Principal principal) {
        teamService.delete(id,principal.getName());
        return redirect("/show");
    }

    @GetMapping("/assign-project/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView assignProject(@PathVariable String id, ModelAndView modelAndView) {
        List<TeamAllViewModel> viewModels = mappingConverter
                .convertCollection(teamService.findAllWithoutProject(),TeamAllViewModel.class);
        modelAndView.addObject("chosenProjectId", id);
        modelAndView.addObject("teams", viewModels);
        return view("/team/choose", modelAndView);
    }

    @PostMapping("/assign-project/{projectId}/{teamId}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView assignProjectConfirm(@PathVariable String projectId,
                                             @PathVariable String teamId, Principal principal) {
        teamService.assignProject(projectId, teamId,principal.getName());
        return redirect("/home");
    }
}
