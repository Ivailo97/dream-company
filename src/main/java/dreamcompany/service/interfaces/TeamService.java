package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.TeamServiceModel;

import java.io.IOException;
import java.util.List;

public interface TeamService {

    TeamServiceModel create(TeamServiceModel teamServiceModel,String moderatorUsername);

    TeamServiceModel edit(String id, TeamServiceModel teamServiceModel,String moderatorUsername) throws IOException;

    TeamServiceModel delete(String id,String moderatorUsername) throws IOException;

    TeamServiceModel findById(String id);

    List<TeamServiceModel> findAll();

    void assignProject(String projectId, String teamId,String managerUsername);

    List<TeamServiceModel> findAllWithoutProject();

    void addEmployeeToTeam(String teamId, String userId,String moderatorUsername);

    void removeEmployeeFromTeam(String teamId, String userId,String moderatorUsername);
}
