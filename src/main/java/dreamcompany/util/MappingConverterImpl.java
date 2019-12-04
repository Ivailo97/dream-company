package dreamcompany.util;

import dreamcompany.domain.entity.Role;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import dreamcompany.domain.model.binding.TeamEditBindingModel;
import dreamcompany.domain.model.binding.UserEditBindingModel;
import dreamcompany.domain.model.service.*;
import dreamcompany.domain.model.view.*;
import dreamcompany.service.interfaces.CloudinaryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MappingConverterImpl implements MappingConverter {

    private final ModelMapper modelMapper;

    private final CloudinaryService cloudinaryService;

    @Override
    public <M, D> D map(M model, Class<D> destinationClass) {
        return modelMapper.map(model, destinationClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <M, D> D convert(M model, Class<D> destinationClass) {

        D result = null;

        switch (destinationClass.getSimpleName()) {

            case "TeamDeleteViewModel":
                result = (D) convertToTeamDeleteViewModel((TeamServiceModel) model);
                break;
            case "TeamDetailsViewModel":
                result = (D) convertToTeamDetailsViewModel((TeamServiceModel) model);
                break;
            case "TeamServiceModel":

                if (model.getClass().getSimpleName().equals("TeamEditBindingModel")) {
                    result = (D) convertToTeamServiceModel((TeamEditBindingModel) model);
                } else {
                    result = (D) convertToTeamServiceModel((TeamCreateBindingModel) model);
                }
                break;
            case "ProjectDetailsViewModel":
                result = (D) convertToProjectDetailsViewModel((ProjectServiceModel) model);
                break;
            case "ProjectDeleteViewModel":
                result = (D) convertToProjectDeleteViewModel((ProjectServiceModel) model);
                break;
            case "TaskDetailsViewModel":
                result = (D) convertToTaskDetailsViewModel((TaskServiceModel) model);
                break;
            case "UserServiceModel":
                if (model.getClass().getSimpleName().equals("User")) {
                    result = (D) convertToUserServiceModel((User) model);
                } else {
                    result = (D) convertToUserServiceModel((UserEditBindingModel) model);
                }
                break;
            case "UserProfileViewModel":
                result = (D) convertToUserProfileViewModel((UserServiceModel) model);
                break;

        }

        return result;
    }

    private UserProfileViewModel convertToUserProfileViewModel(UserServiceModel model) {

        UserProfileViewModel viewModel = modelMapper.map(model, UserProfileViewModel.class);

        List<UserServiceModel> serviceModelFriends = new ArrayList<>(model.getFriends());

        for (int i = 0; i < model.getFriends().size(); i++) {
            FriendViewModel friendViewModel = viewModel.getFriends().get(i);
            UserServiceModel userServiceModel = serviceModelFriends.get(i);
            friendViewModel.setFullName(String.format("%s %s", userServiceModel.getFirstName(), userServiceModel.getLastName()));
        }

        return viewModel;
    }


    private UserServiceModel convertToUserServiceModel(User model) {

        UserServiceModel userServiceModel = modelMapper.map(model, UserServiceModel.class);

        userServiceModel.setUsername(model.getUsername());
        userServiceModel.setFirstName(model.getFirstName());
        userServiceModel.setLastName(model.getLastName());
        userServiceModel.setEmail(model.getEmail());
        userServiceModel.setPassword(model.getPassword());
        userServiceModel.setId(model.getId());
        userServiceModel.setCredits(model.getCredits());
        userServiceModel.setImageUrl(model.getImageUrl());
        userServiceModel.setImageId(model.getImageId());
        userServiceModel.setPosition(model.getPosition());
        userServiceModel.setSalary(model.getSalary());
        userServiceModel.setHiredOn(model.getHiredOn());

        Set<Role> roles = model.getAuthorities();

        Set<RoleServiceModel> serviceModels = roles.stream()
                .map(r -> modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());

        userServiceModel.setAuthorities(serviceModels);
        userServiceModel.setFriendRequests(model.getFriendRequests().stream()
                .map(fr -> modelMapper.map(fr, FriendRequestServiceModel.class))
                .collect(Collectors.toSet()));

        return userServiceModel;
    }


    @Override
    @SuppressWarnings("unchecked")
    public <M, D> List<D> convertCollection(List<M> collection, Class<D> destinationClass) {

        List<D> result = new ArrayList<>();

        switch (destinationClass.getSimpleName()) {

            case "TeamAllViewModel":
                result = (List<D>) convertToTeamAllViewModels((List<TeamServiceModel>) collection);
                break;
            case "UserAddRemoveFromTeamViewModel":
                result = (List<D>) convertToUserAddRemoveFromTeamViewModels((List<UserServiceModel>) collection);
                break;
            case "OfficeAllViewModel":
                result = (List<D>) convertToOfficeAllViewModels((List<OfficeServiceModel>) collection);
                break;
            case "OfficeFetchViewModel":
                result = (List<D>) convertToOfficeFetchViewModels((List<OfficeServiceModel>) collection);
                break;
            case "ProjectAllViewModel":
                result = (List<D>) convertToProjectAllViewModels((List<ProjectServiceModel>) collection);
                break;
            case "TaskAssignViewModel":
                result = (List<D>) convertToTaskAssignViewModels((List<TaskServiceModel>) collection);
                break;
            case "ProjectFetchViewModel":
                result = (List<D>) convertToProjectFetchViewModels((List<ProjectServiceModel>) collection);
                break;
            case "TaskFetchViewModel":
                result = (List<D>) convertToTaskFetchViewModels((List<TaskServiceModel>) collection);
                break;
            case "TaskAllViewModel":
                result = (List<D>) convertToTaskAllViewModels((List<TaskServiceModel>) collection);
                break;
            case "UserAllViewModel":
                result = (List<D>) convertToUserAllViewModels((List<UserServiceModel>) collection);
                break;
            case "UserAssignTaskViewModel":
                result = (List<D>) convertToUserAssignTaskViewModels((List<UserServiceModel>) collection);
                break;
            case "TaskAssignedViewModel":
                result = (List<D>) convertToTaskAssignedViewModels((List<TaskServiceModel>) collection);
                break;
            case "UserPositionChangeViewModel":
                result = (List<D>) convertToUserPositionChangeViewModels((List<UserServiceModel>) collection);
                break;
            case "UserFetchViewModel":
                result = (List<D>) convertToUserFetchViewModels((List<UserServiceModel>) collection);
                break;
        }

        return result;
    }

    private TeamDeleteViewModel convertToTeamDeleteViewModel(TeamServiceModel model) {

        TeamDeleteViewModel teamDeleteViewModel = modelMapper.map(model, TeamDeleteViewModel.class);

        teamDeleteViewModel.setOffice(model.getOffice().getAddress());
        teamDeleteViewModel.setEmployees(model.getEmployees().stream()
                .map(e -> String.format("%s %s", e.getFirstName(), e.getLastName()))
                .collect(Collectors.toSet()));

        return teamDeleteViewModel;
    }


    private TeamDetailsViewModel convertToTeamDetailsViewModel(TeamServiceModel model) {
        TeamDetailsViewModel viewModel = modelMapper.map(model, TeamDetailsViewModel.class);
        Set<UserInTeamDetailsViewModel> employees = new HashSet<>();

        model.getEmployees()
                .forEach(e -> {
                    UserInTeamDetailsViewModel userViewModel = modelMapper.map(e, UserInTeamDetailsViewModel.class);
                    userViewModel.setFullName(String.format("%s %s", e.getFirstName(), e.getLastName()));
                    employees.add(userViewModel);
                });

        viewModel.setEmployees(employees);

        return viewModel;
    }


    private List<UserAddRemoveFromTeamViewModel> convertToUserAddRemoveFromTeamViewModels(List<UserServiceModel> models) {
        return models
                .stream()
                .map(u -> modelMapper.map(u, UserAddRemoveFromTeamViewModel.class))
                .collect(Collectors.toList());
    }


    private TeamServiceModel convertToTeamServiceModel(TeamEditBindingModel model) {

        TeamServiceModel teamServiceModel = modelMapper.map(model, TeamServiceModel.class);

        if (!model.getLogo().isEmpty()) {
            String[] uploadInfo = cloudinaryService.uploadImage(model.getLogo());
            teamServiceModel.setLogoUrl(uploadInfo[0]);
            teamServiceModel.setLogoId(uploadInfo[1]);
        }

        //set office
        OfficeServiceModel officeServiceModel = new OfficeServiceModel();
        officeServiceModel.setId(model.getOffice());
        teamServiceModel.setOffice(officeServiceModel);

        return teamServiceModel;
    }


    private TeamServiceModel convertToTeamServiceModel(TeamCreateBindingModel model) {

        TeamServiceModel teamServiceModel = modelMapper.map(model, TeamServiceModel.class);

        String[] uploadInfo = cloudinaryService.uploadImage(model.getLogo());
        teamServiceModel.setLogoUrl(uploadInfo[0]);
        teamServiceModel.setLogoId(uploadInfo[1]);

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


    private List<OfficeAllViewModel> convertToOfficeAllViewModels(List<OfficeServiceModel> models) {
        return models.stream()
                .map(x -> modelMapper.map(x, OfficeAllViewModel.class))
                .collect(Collectors.toList());
    }


    private List<OfficeFetchViewModel> convertToOfficeFetchViewModels(List<OfficeServiceModel> models) {
        return models.stream()
                .map(o -> modelMapper.map(o, OfficeFetchViewModel.class))
                .collect(Collectors.toList());
    }


    private ProjectDetailsViewModel convertToProjectDetailsViewModel(ProjectServiceModel model) {
        ProjectDetailsViewModel viewModel = modelMapper.map(model, ProjectDetailsViewModel.class);
        viewModel.setTasks(model.getTasks().stream().map(TaskServiceModel::getName).collect(Collectors.toSet()));
        return viewModel;
    }


    private ProjectDeleteViewModel convertToProjectDeleteViewModel(ProjectServiceModel model) {
        ProjectDeleteViewModel projectDeleteViewModel = modelMapper.map(model, ProjectDeleteViewModel.class);
        projectDeleteViewModel.setTasks(model.getTasks().stream().map(TaskServiceModel::getName).collect(Collectors.toSet()));
        return projectDeleteViewModel;
    }


    private List<ProjectAllViewModel> convertToProjectAllViewModels(List<ProjectServiceModel> models) {
        return models.stream()
                .map(p -> modelMapper.map(p, ProjectAllViewModel.class))
                .collect(Collectors.toList());
    }


    private List<TaskAssignViewModel> convertToTaskAssignViewModels(List<TaskServiceModel> models) {
        return models.stream()
                .map(t -> modelMapper.map(t, TaskAssignViewModel.class))
                .collect(Collectors.toList());
    }


    private List<ProjectFetchViewModel> convertToProjectFetchViewModels(List<ProjectServiceModel> models) {
        return models.stream()
                .map(p -> modelMapper.map(p, ProjectFetchViewModel.class))
                .collect(Collectors.toList());
    }


    private TaskDetailsViewModel convertToTaskDetailsViewModel(TaskServiceModel model) {
        TaskDetailsViewModel taskDetailsViewModel = modelMapper.map(model, TaskDetailsViewModel.class);
        taskDetailsViewModel.setProject(taskDetailsViewModel.getProject());
        return taskDetailsViewModel;
    }


    private List<TaskFetchViewModel> convertToTaskFetchViewModels(List<TaskServiceModel> models) {
        return models.stream()
                .map(t -> modelMapper.map(t, TaskFetchViewModel.class))
                .collect(Collectors.toList());
    }


    private List<TaskAllViewModel> convertToTaskAllViewModels(List<TaskServiceModel> models) {
        return models.stream()
                .map(t -> modelMapper.map(t, TaskAllViewModel.class))
                .collect(Collectors.toList());
    }


    private List<UserAllViewModel> convertToUserAllViewModels(List<UserServiceModel> models) {
        return models.stream()
                .map(u -> modelMapper.map(u, UserAllViewModel.class))
                .collect(Collectors.toList());
    }


    private UserServiceModel convertToUserServiceModel(UserEditBindingModel model) {

        UserServiceModel userServiceModel = modelMapper.map(model, UserServiceModel.class);

        if (!model.getPicture().isEmpty()) {
            String[] uploadInfo = cloudinaryService.uploadImage(model.getPicture());
            userServiceModel.setImageUrl(uploadInfo[0]);
            userServiceModel.setImageId(uploadInfo[1]);
        }

        return userServiceModel;
    }


    private List<UserAssignTaskViewModel> convertToUserAssignTaskViewModels(List<UserServiceModel> models) {
        List<UserAssignTaskViewModel> viewModels = new ArrayList<>();
        models.forEach(u -> {
            UserAssignTaskViewModel viewModel = modelMapper.map(u, UserAssignTaskViewModel.class);
            viewModel.setFullName(String.format("%s %s", u.getFirstName(), u.getLastName()));
            viewModels.add(viewModel);
        });
        return viewModels;
    }


    private List<TaskAssignedViewModel> convertToTaskAssignedViewModels(List<TaskServiceModel> models) {
        return models.stream()
                .map(t -> modelMapper.map(t, TaskAssignedViewModel.class))
                .collect(Collectors.toList());
    }


    private List<UserPositionChangeViewModel> convertToUserPositionChangeViewModels(List<UserServiceModel> models) {
        return models.stream()
                .map(u -> modelMapper.map(u, UserPositionChangeViewModel.class))
                .collect(Collectors.toList());
    }


    private List<UserFetchViewModel> convertToUserFetchViewModels(List<UserServiceModel> models) {
        return models.stream()
                .map(u -> {
                    UserFetchViewModel model = modelMapper.map(u, UserFetchViewModel.class);
                    model.setFullName(String.format("%s %s", u.getFirstName(), u.getLastName()));
                    return model;
                })
                .collect(Collectors.toList());
    }


    private List<TeamAllViewModel> convertToTeamAllViewModels(List<TeamServiceModel> models) {
        return models.stream()
                .map(t -> modelMapper.map(t, TeamAllViewModel.class))
                .collect(Collectors.toList());
    }
}
