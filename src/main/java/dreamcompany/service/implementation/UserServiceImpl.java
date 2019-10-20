package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.repository.TaskRepository;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.RoleService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MyThread;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import dreamcompany.domain.entity.User;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.repository.UserRepository;

import javax.management.relation.RoleNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final CloudinaryService cloudinaryService;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository, CloudinaryService cloudinaryService, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.roleService = roleService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) throws RoleNotFoundException {

        defineUserRolesAndPosition(userServiceModel);
        userServiceModel.setHiredOn(LocalDateTime.now());
        userServiceModel.setPassword(encoder.encode(userServiceModel.getPassword()));

        User entity = modelMapper.map(userServiceModel, User.class);

        entity = userRepository.save(entity);

        return modelMapper.map(entity, UserServiceModel.class);
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    @Override
    public UserServiceModel edit(UserServiceModel userServiceModel, String oldPassword) throws IOException {

        User user = userRepository.findByUsername(userServiceModel.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password!");
        }

        if (user.getImageUrl() != null) {
            cloudinaryService.deleteImage(user.getImageId());
        }

        user.setUsername(userServiceModel.getUsername());
        user.setPassword(encoder.encode(userServiceModel.getPassword()));
        user.setEmail(userServiceModel.getEmail());
        user.setFirstName(userServiceModel.getFirstName());
        user.setLastName(userServiceModel.getLastName());

        if (userServiceModel.getImageUrl() != null) {
            user.setImageUrl(userServiceModel.getImageUrl());
            user.setImageId(userServiceModel.getImageId());
        }

        return modelMapper.map(userRepository.saveAndFlush(user), UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAll() {
        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllNonLeadersWithoutTeam() {
        return userRepository
                .findAllByTeamNullAndPositionNotIn(Position.TEAM_LEADER, Position.PROJECT_MANAGER)
                .stream()
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllWithoutTeam() {
        return userRepository
                .findAllByTeamNullAndPositionNotIn(Position.PROJECT_MANAGER)
                .stream()
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllInTeam(String teamId) {

        return userRepository.findAllByTeamId(teamId)
                .stream()
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllInTeamWithPosition(String teamId, Position position) {

        return userRepository.findAllByTeamIdAndPosition(teamId, position)
                .stream().map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllForPromotion() {
        return userRepository.findAllByCreditsGreaterThanAndPositionNotIn(GlobalConstraints.MAX_CREDITS - 1, Position.SENIOR, Position.PROJECT_MANAGER, Position.TEAM_LEADER)
                .stream().map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserServiceModel> findAllForDemotion() {
        return userRepository.findAllByPositionNotIn(Position.PROJECT_MANAGER, Position.TEAM_LEADER, Position.INTERN)
                .stream().map(u -> modelMapper.map(u, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isLeaderWithAssignedProject(String username) {

        UserServiceModel userServiceModel = findByUsername(username);

        return userServiceModel.getPosition() == Position.TEAM_LEADER
                && userServiceModel.getTeam() != null
                && userServiceModel.getTeam().getProject() != null;
    }

    @Override
    public void assignTask(String userId, String taskId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        if (user.getAssignedTasks() == null) {
            user.setAssignedTasks(new LinkedHashSet<>());
        }

        task.setEmployee(user);
        task.setStatus(Status.IN_PROGRESS);

        taskRepository.save(task);
    }

    @Override
    public void completeTask(String userId, String taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task id"));

        long milliseconds = TimeUnit.MINUTES.toMillis(task.getMinutesNeeded());

        Thread thread = new MyThread(milliseconds, () -> actualTaskComplete(user, task));

        thread.start();
    }

    @Override
    public void changeRoles(String userId, String role) throws RoleNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found! Wrong user id!"));

        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);

        userServiceModel.getAuthorities().clear();

        switch (role) {

            case "user":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                break;
            case "moderator":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
                break;
            case "admin":
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
                userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_ADMIN"));
                break;
        }

        userRepository.saveAndFlush(modelMapper.map(userServiceModel, User.class));
    }

    @Override
    public void promote(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id"));

        int nextPositionIndex = user.getPosition().ordinal() + 1;

        Position nextPosition = Position.values()[nextPositionIndex];
        user.setPosition(nextPosition);
        user.setCredits(user.getCredits() - GlobalConstraints.MAX_CREDITS);
        user.setSalary(nextPosition.getSalary());

        userRepository.save(user);
    }

    @Override
    public void demote(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id"));

        int nextPositionIndex = user.getPosition().ordinal() - 1;

        Position nextPosition = Position.values()[nextPositionIndex];
        user.setPosition(nextPosition);
        user.setCredits(GlobalConstraints.STARTING_CREDITS);
        user.setSalary(nextPosition.getSalary());

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(("Username not found!")));
    }

    private void defineUserRolesAndPosition(UserServiceModel userServiceModel) throws RoleNotFoundException {

        roleService.seedRoles();
        userServiceModel.setAuthorities(new HashSet<>());

        long repositoryCount = userRepository.count();

        if (repositoryCount == 0) {
            userServiceModel.setPosition(Position.PROJECT_MANAGER);
            userServiceModel.setSalary(GlobalConstraints.PROJECT_MANAGER_SALARY);
            userServiceModel.setCredits(GlobalConstraints.MAX_CREDITS);
            userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_ROOT"));
            userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_ADMIN"));
            userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_MODERATOR"));
            userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
        } else {
            userServiceModel.setPosition(Position.INTERN);
            userServiceModel.setSalary(GlobalConstraints.INTERN_SALARY);
            userServiceModel.setCredits(GlobalConstraints.STARTING_CREDITS);
            userServiceModel.getAuthorities().add(roleService.findByAuthority("ROLE_USER"));
        }
    }

    private void actualTaskComplete(User user, Task task) {

        user.setCredits(user.getCredits() + task.getCredits());

        userRepository.save(user);

        task.setStatus(Status.FINISHED);

        taskRepository.save(task);
    }
}
