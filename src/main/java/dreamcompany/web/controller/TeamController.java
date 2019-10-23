package dreamcompany.web.controller;

import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import dreamcompany.domain.model.binding.TeamEditBindingModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.TeamService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MappingConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/teams")
@PreAuthorize("isAuthenticated()")
public class TeamController extends BaseController {

    private final TeamService teamService;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final MappingConverter mappingConverter;

    @Autowired
    public TeamController(TeamService teamService, UserService userService, ModelMapper modelMapper, MappingConverter mappingConverter) {
        this.teamService = teamService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.mappingConverter = mappingConverter;
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView create(@ModelAttribute(name = "model") TeamCreateBindingModel model) {
        return view("/team/create");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView createConfirm(@Valid @ModelAttribute(name = "model") TeamCreateBindingModel model,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view("/team/create");
        }

        TeamServiceModel teamServiceModel = mappingConverter.convertToTeamServiceModel(model);
        teamService.create(teamServiceModel);
        return redirect("/home");
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {

        TeamServiceModel teamServiceModel = teamService.findById(id);
        TeamEditBindingModel teamEditBindingModel = modelMapper.map(teamServiceModel, TeamEditBindingModel.class);
        teamEditBindingModel.setOffice(teamServiceModel.getOffice().getId());
        modelAndView.addObject("model", teamEditBindingModel);
        return view("/team/edit", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute(name = "model") TeamEditBindingModel model,
                                    BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return view("/team/edit");
        }

        TeamServiceModel teamServiceModel = mappingConverter.convertToTeamServiceModel(model);
        teamService.edit(id, teamServiceModel);
        return redirect("/home");
    }

    @GetMapping("/add-employee/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addEmployee(@PathVariable String id, ModelAndView modelAndView) {

        List<UserAddRemoveFromTeamViewModel> viewModels = mappingConverter.convertToUserAddRemoveFromTeamViewModels(userService.findAllWithoutTeam());
        modelAndView.addObject("teamId", id);
        modelAndView.addObject("models", viewModels);
        return view("add-employee", modelAndView);
    }

    @PostMapping("/add-employee/{teamId}/{userId}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView addEmployeeConfirm(@PathVariable String teamId, @PathVariable String userId) {

        teamService.addEmployeeToTeam(teamId, userId);
        return redirect("/teams/all");
    }

    @GetMapping("/remove-employee/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView removeEmployee(@PathVariable String id, ModelAndView modelAndView) {

        List<UserAddRemoveFromTeamViewModel> viewModels = mappingConverter.convertToUserAddRemoveFromTeamViewModels(userService.findAllInTeam(id));
        modelAndView.addObject("teamId", id);
        modelAndView.addObject("models", viewModels);
        return view("remove-employee", modelAndView);
    }

    @PostMapping("/remove-employee/{teamId}/{userId}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView removeEmployeeConfirm(@PathVariable String teamId, @PathVariable String userId) {

        teamService.removeEmployeeFromTeam(teamId, userId);
        return redirect("/teams/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView all(ModelAndView modelAndView) {

        List<TeamAllViewModel> viewModels = mappingConverter.convertToTeamAllViewModels(teamService.findAll());
        modelAndView.addObject("models", viewModels);
        return view("/team/all", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {

        TeamServiceModel teamServiceModel = teamService.findById(id);
        TeamDetailsViewModel viewModel = mappingConverter.convertToTeamDetailsViewModel(teamServiceModel);
        modelAndView.addObject("model", viewModel);
        return view("/team/details", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {

        TeamServiceModel teamServiceModel = teamService.findById(id);
        TeamDeleteViewModel teamDeleteViewModel = mappingConverter.convertToTeamDeleteViewModel(teamServiceModel);
        modelAndView.addObject("model", teamDeleteViewModel);
        return view("/team/delete", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {

        teamService.delete(id);
        return redirect("/teams/all");
    }

    @GetMapping("/assign-project/{id}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView assignProject(@PathVariable String id, ModelAndView modelAndView) {

        List<TeamAllViewModel> viewModels = mappingConverter.convertToTeamAllViewModels(teamService.findAllWithoutProject());
        modelAndView.addObject("chosenProjectId", id);
        modelAndView.addObject("teams", viewModels);
        return view("/team/choose", modelAndView);
    }

    @PostMapping("/assign-project/{projectId}/{teamId}")
    @PreAuthorize("hasRole('ROLE_ROOT')")
    public ModelAndView assignProjectConfirm(@PathVariable String projectId, @PathVariable String teamId) {

        teamService.assignProject(projectId, teamId);
        return redirect("/projects/manage");
    }
}
