package dreamcompany.service.implementation.role;

import dreamcompany.domain.entity.Role;
import dreamcompany.domain.model.service.RoleServiceModel;
import dreamcompany.repository.RoleRepository;
import dreamcompany.service.implementation.RoleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import javax.management.relation.RoleNotFoundException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTests {

    private static final String ROLE_ROOT = "ROLE_ROOT";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    private static final String ROLE_USER = "ROLE_USER";

    private static List<Role> fakeRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Before
    public void init() {

        fakeRepository = new ArrayList<>();

        //instead of saving in db add in fake repository
        when(roleRepository.save(any(Role.class)))
                .thenAnswer(invocationOnMock -> {
                    fakeRepository.add((Role) invocationOnMock.getArguments()[0]);
                    return null;
                });

        //actual mappings
        ModelMapper actualMapper = new ModelMapper();
        when(modelMapper.map(any(Role.class), eq(RoleServiceModel.class)))
                .thenAnswer(invocationOnMock -> actualMapper.map(invocationOnMock.getArguments()[0], RoleServiceModel.class));
    }

    @Test
    public void seedRoles_shouldSeedAllRoles_whenRepositoryEmpty() {

        //Arrange
        when(roleRepository.count()).thenReturn(0L);

        //Act
        roleService.seedRoles();

        //Assert
        int expected = 4;
        int actual = fakeRepository.size();
        assertEquals(expected, actual);
        assertEquals(fakeRepository.get(0).getAuthority(), ROLE_ROOT);
        assertEquals(fakeRepository.get(1).getAuthority(), ROLE_ADMIN);
        assertEquals(fakeRepository.get(2).getAuthority(), ROLE_MODERATOR);
        assertEquals(fakeRepository.get(3).getAuthority(), ROLE_USER);
    }

    @Test
    public void seedRoles_shouldDoNothing_whenRepositoryNotEmpty() {

        //Arrange
        when(roleRepository.count()).thenReturn(4L);

        //Act
        roleService.seedRoles();

        //Assert
        int expected = 0;
        int actual = fakeRepository.size();
        assertEquals(expected, actual);
    }

    @Test
    public void findByAuthority_shouldReturnCorrectRoleServiceModel_whenAuthorityExist() throws RoleNotFoundException {

        //Arrange
        //make sure such role is found in repository layer
        when(roleRepository.findByAuthority(ROLE_ROOT))
                .thenReturn(Optional.of(new Role(ROLE_ROOT)));

        //Act
        RoleServiceModel roleServiceModel = roleService.findByAuthority(ROLE_ROOT);

        //Assert
        String actual = roleServiceModel.getAuthority();
        assertEquals(ROLE_ROOT, actual);
    }

    @Test(expected = RoleNotFoundException.class)
    public void findByAuthority_shouldThrowException_whenAuthorityNotExist() throws RoleNotFoundException {

        //Arrange
        //make sure such role is not found in repository layer
        when(roleRepository.findByAuthority(ROLE_ROOT))
                .thenReturn(Optional.empty());

        //Act
        roleService.findByAuthority(ROLE_ROOT);
    }

    @Test
    public void findAll_shouldReturnAllServiceModelsCorrect_whenAnyRolesInDb() {

        //Arrange
        //make sure all roles are found in repository layer
        List<Role> allRoles = Arrays.asList(new Role(ROLE_ROOT), new Role(ROLE_ADMIN), new Role(ROLE_MODERATOR), new Role(ROLE_USER));
        when(roleRepository.findAll()).thenReturn(allRoles);

        //Act
        Set<RoleServiceModel> allFoundRoleServiceModels = roleService.findAll();

        //Assert
        int expected = allRoles.size();
        int actual = allFoundRoleServiceModels.size();
        assertEquals(expected,actual);
    }

    @Test
    public void findAll_shouldReturnEmptyCollection_whenNoRolesInDb(){

        //Arrange
        //make sure all roles are found in repository layer
        List<Role> allRoles = new ArrayList<>();
        when(roleRepository.findAll()).thenReturn(allRoles);

        //Act
        Set<RoleServiceModel> allFoundRoleServiceModels = roleService.findAll();

        //Assert
        int expected = allRoles.size();
        int actual = allFoundRoleServiceModels.size();
        assertEquals(expected,actual);
    }
}
