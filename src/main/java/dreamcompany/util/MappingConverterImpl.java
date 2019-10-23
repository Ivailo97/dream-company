package dreamcompany.util;

import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import dreamcompany.domain.model.binding.TeamEditBindingModel;
import dreamcompany.domain.model.service.OfficeServiceModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.domain.model.view.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MappingConverterImpl implements MappingConverter {

    private final ModelMapper modelMapper;

    @Autowired
    public MappingConverterImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TeamDeleteViewModel convertToTeamDeleteViewModel(TeamServiceModel teamServiceModel) {

        TeamDeleteViewModel teamDeleteViewModel = modelMapper.map(teamServiceModel, TeamDeleteViewModel.class);

        teamDeleteViewModel.setOffice(teamServiceModel.getOffice().getAddress());
        teamDeleteViewModel.setEmployees(teamServiceModel.getEmployees().stream()
                .map(e -> String.format("%s %s", e.getFirstName(), e.getLastName()))
                .collect(Collectors.toSet()));

        return teamDeleteViewModel;
    }

    @Override
    public TeamDetailsViewModel convertToTeamDetailsViewModel(TeamServiceModel teamServiceModel) {
        TeamDetailsViewModel viewModel = modelMapper.map(teamServiceModel, TeamDetailsViewModel.class);
        Set<UserInTeamDetailsViewModel> employees = new HashSet<>();

        teamServiceModel.getEmployees()
                .forEach(e -> {
                    UserInTeamDetailsViewModel userViewModel = modelMapper.map(e, UserInTeamDetailsViewModel.class);
                    userViewModel.setFullName(String.format("%s %s", e.getFirstName(), e.getLastName()));
                    employees.add(userViewModel);
                });

        viewModel.setEmployees(employees);

        return viewModel;
    }

    @Override
    public List<UserAddRemoveFromTeamViewModel> convertToUserAddRemoveFromTeamViewModels(List<UserServiceModel> allWithoutTeam) {
        return allWithoutTeam
                .stream()
                .map(u -> modelMapper.map(u, UserAddRemoveFromTeamViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public TeamServiceModel convertToTeamServiceModel(TeamEditBindingModel model) {

        TeamServiceModel teamServiceModel = modelMapper.map(model, TeamServiceModel.class);

        //set office
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        officeServiceModel.setId(model.getOffice());
        teamServiceModel.setOffice(officeServiceModel);

        return teamServiceModel;
    }

    @Override
    public TeamServiceModel convertToTeamServiceModel(TeamCreateBindingModel model) {

        TeamServiceModel teamServiceModel = modelMapper.map(model, TeamServiceModel.class);

        //set office
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        officeServiceModel.setId(model.getOffice());
        teamServiceModel.setOffice(officeServiceModel);

        //setEmployees
        Set<UserServiceModel> employees = new LinkedHashSet<>();
        model.getEmployees().forEach(e -> {
            UserServiceModel employee = new UserServiceModel();
            employee.setId(e);
            employees.add(employee);
        });
        teamServiceModel.setEmployees(employees);

        return teamServiceModel;
    }

    @Override
    public List<TeamAllViewModel> convertToTeamAllViewModels(List<TeamServiceModel> allWithoutProject) {
        return allWithoutProject
                .stream()
                .map(t -> modelMapper.map(t, TeamAllViewModel.class))
                .collect(Collectors.toList());
    }
}
