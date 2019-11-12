package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.TeamServiceModel;

import java.io.IOException;
import java.util.List;

public interface TeamService {

    TeamServiceModel create(TeamServiceModel teamServiceModel);

    TeamServiceModel edit(String id, TeamServiceModel teamServiceModel) throws IOException;

    TeamServiceModel delete(String id) throws IOException;

    TeamServiceModel findById(String id);

    List<TeamServiceModel> findAll();

    void assignProject(String projectId, String teamId);

    List<TeamServiceModel> findAllWithoutProject();

    void addEmployeeToTeam(String teamId, String userId);

    void removeEmployeeFromTeam(String teamId, String userId);
}
