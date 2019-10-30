package dreamcompany.service.implementation;

import dreamcompany.domain.entity.Role;
import dreamcompany.domain.model.service.RoleServiceModel;
import dreamcompany.repository.RoleRepository;
import dreamcompany.service.interfaces.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedRoles() {

        if (this.roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_ROOT"));
            roleRepository.save(new Role("ROLE_ADMIN"));
            roleRepository.save(new Role("ROLE_MODERATOR"));
            roleRepository.save(new Role("ROLE_USER"));
        }
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) throws RoleNotFoundException {

        return this.roleRepository.findByAuthority(authority)
                .map(r-> this.modelMapper.map(r,RoleServiceModel.class))
                .orElseThrow(()-> new RoleNotFoundException("Role not found!"));
    }

    @Override
    public Set<RoleServiceModel> findAll() {
        return this.roleRepository.findAll().stream()
                .map(r -> this.modelMapper.map(r, RoleServiceModel.class))
                .collect(Collectors.toSet());
    }
}
