package dreamcompany.service.interfaces;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel register(UserServiceModel userServiceModel) throws RoleNotFoundException;

    UserServiceModel edit(UserServiceModel userServiceModel, String oldPassword) throws IOException;

    UserServiceModel findByUsername(String username);

    List<UserServiceModel> findAll();

    List<UserServiceModel> findAllNonLeadersWithoutTeam();

    List<UserServiceModel> findAllWithoutTeam();

    List<UserServiceModel> findAllInTeam(String teamId);

    List<UserServiceModel> findAllInTeamWithPosition(String teamId,Position position);

    List<UserServiceModel> findAllForPromotion();

    List<UserServiceModel> findAllForDemotion();

    boolean isLeaderWithAssignedProject(String username);

    void assignTask(String userId, String taskId);

    void completeTask(String userId,String taskId);

    void changeRoles(String userId, String authority,String adminUsername) throws RoleNotFoundException;

    void promote(String userId,String rootUsername);

    void demote(String userId,String rootUsername);


    //surprise unit testing incoming:))))))
    boolean canRemoveFriend(String username,String friendUsername);

    void removeFriend(String name, String friendUsername);
}
