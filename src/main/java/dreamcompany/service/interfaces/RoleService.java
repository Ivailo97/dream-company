package dreamcompany.service.interfaces;

import dreamcompany.domain.model.service.RoleServiceModel;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;

public interface RoleService {

    void seedRoles();

    RoleServiceModel findByAuthority(String authority) throws RoleNotFoundException;

    Set<RoleServiceModel> findAll();
}
