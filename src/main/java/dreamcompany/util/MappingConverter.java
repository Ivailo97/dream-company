package dreamcompany.util;

import dreamcompany.domain.model.binding.TeamCreateBindingModel;
import dreamcompany.domain.model.binding.TeamEditBindingModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.domain.model.view.TeamAllViewModel;
import dreamcompany.domain.model.view.TeamDeleteViewModel;
import dreamcompany.domain.model.view.TeamDetailsViewModel;
import dreamcompany.domain.model.view.UserAddRemoveFromTeamViewModel;

import java.util.List;

public interface MappingConverter {

    List<TeamAllViewModel> convertToTeamAllViewModels(List<TeamServiceModel> allWithoutProject);

    TeamDeleteViewModel convertToTeamDeleteViewModel(TeamServiceModel teamServiceModel);

    TeamDetailsViewModel convertToTeamDetailsViewModel(TeamServiceModel teamServiceModel);

    List<UserAddRemoveFromTeamViewModel> convertToUserAddRemoveFromTeamViewModels(List<UserServiceModel> allWithoutTeam);

    TeamServiceModel convertToTeamServiceModel(TeamEditBindingModel model);

    TeamServiceModel convertToTeamServiceModel(TeamCreateBindingModel model);
}
