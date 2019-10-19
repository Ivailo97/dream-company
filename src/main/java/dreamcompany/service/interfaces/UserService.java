package dreamcompany.service.interfaces;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel register(UserServiceModel userServiceModel) throws RoleNotFoundException;

    UserServiceModel findByUsername(String username);

    UserServiceModel edit(UserServiceModel userServiceModel, String oldPassword) throws IOException;

    List<UserServiceModel> findAll();

    List<UserServiceModel> findAllNonLeadersWithoutTeam();

    List<UserServiceModel> findAllWithoutTeam();

    List<UserServiceModel> findAllInTeam(String teamId);

    List<UserServiceModel> findAllInTeamWithPosition(String teamId,Position position);

    boolean isLeaderWithAssignedProject(String username);

    void assignTask(String userId, String taskId);

    void completeTask(String userId,String taskId);

    void changeRoles(String userId, String authority) throws RoleNotFoundException;

    void promote(String userId);

    void demote(String userId);
}
