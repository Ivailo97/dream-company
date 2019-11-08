package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.error.duplicates.EmailAlreadyExistException;
import dreamcompany.error.duplicates.UsernameAlreadyExistException;
import dreamcompany.error.WrongOldPasswordException;
import dreamcompany.error.invalidservicemodels.InvalidUserServiceModelException;
import dreamcompany.error.notexist.TaskNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.TaskRepository;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.LogService;
import dreamcompany.service.interfaces.RoleService;
import dreamcompany.service.interfaces.UserService;
import dreamcompany.util.MyThread;
import dreamcompany.validation.service.interfaces.UserValidationService;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static dreamcompany.GlobalConstraints.*;

@Service
public class UserServiceImpl implements UserService {

    private static final String USER_CHANGE_ROLE = "user";
    private static final String ADMIN_CHANGE_ROLE = "admin";
    private static final String MODERATOR_CHANGE_ROLE = "moderator";

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    private final CloudinaryService cloudinaryService;

    private final LogService logService;

    private final RoleService roleService;

    private final ModelMapper modelMapper;

    private final BCryptPasswordEncoder encoder;

    private final UserValidationService userValidationService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TaskRepository taskRepository, CloudinaryService cloudinaryService, LogService logService, RoleService roleService, ModelMapper modelMapper, BCryptPasswordEncoder encoder, UserValidationService userValidationService) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.logService = logService;
        this.roleService = roleService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
        this.userValidationService = userValidationService;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) throws RoleNotFoundException {

        String username = userServiceModel.getUsername();
        //checks for username taken or email taken
        throwIfUserExist(username, userServiceModel.getEmail());
        defineUserRolesAndPosition(userServiceModel);

        userServiceModel.setHiredOn(LocalDateTime.now());
        userServiceModel.setPassword(encoder.encode(userServiceModel.getPassword()));
        //checks for null fields
        throwIfServiceModelNotValid(userServiceModel);

        User entity = modelMapper.map(userServiceModel, User.class);

        entity = userRepository.save(entity);

        String logMessage = String.format(REGISTERED_USER_SUCCESSFULLY, username, entity.getId());

        logAction(entity.getUsername(), logMessage);

        return modelMapper.map(entity, UserServiceModel.class);
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(u -> modelMapper.map(u, UserServiceModel.class))
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));
    }

    @Override
    public UserServiceModel edit(UserServiceModel edited, String oldPassword) throws IOException {

        //checks for null fields
        throwIfServiceModelNotValid(edited);
        User user = userRepository.findByUsername(edited.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND));

        throwIfWrongOldPassword(oldPassword, user.getPassword());

        throwIfUpdatedWithTakenEmail(user.getEmail(), edited.getEmail());

        boolean imageIsUpdated = updateImage(user, edited);
        String logMessage = buildUpdatedEntityLogMessage(user, edited, imageIsUpdated);
        user.setPassword(encoder.encode(edited.getPassword()));
        user.setEmail(edited.getEmail());
        user.setFirstName(edited.getFirstName());
        user.setLastName(edited.getLastName());

        logAction(user.getUsername(), logMessage);

        return modelMapper.map(userRepository.save(user), UserServiceModel.class);
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
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_MESSAGE));

        if (user.getAssignedTasks() == null) {
            user.setAssignedTasks(new LinkedHashSet<>());
        }

        task.setEmployee(user);
        task.setStatus(Status.IN_PROGRESS);

        String teamLeaderUsername = user.getTeam()
                .getEmployees()
                .stream()
                .filter(u -> u.getPosition() == Position.TEAM_LEADER)
                .map(User::getUsername)
                .findFirst().orElse(null);

        String logMessage = String.format(ASSIGNED_TASK_SUCCESSFULLY, task.getName(), user.getUsername());

        logAction(teamLeaderUsername, logMessage);

        taskRepository.save(task);
    }

    @Override
    public void completeTask(String userId, String taskId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND_MESSAGE));

        actualTaskComplete(user,task);

        String logMessage = String.format(COMPLETED_TASK_SUCCESSFULLY, user.getUsername(), task.getName());
        logAction(user.getUsername(), logMessage);
    }

    @Override
    public void changeRoles(String userId, String role, String adminUsername) throws RoleNotFoundException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        UserServiceModel userServiceModel = modelMapper.map(user, UserServiceModel.class);

        userServiceModel.getAuthorities().clear();
        String username = user.getUsername();

        String logMessage;

        switch (role) {

            case USER_CHANGE_ROLE:
                userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_USER));
                logMessage = String.format(CHANGED_ROLE_SUCCESSFULLY, username, ROLE_USER);
                break;
            case MODERATOR_CHANGE_ROLE:
                userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_USER));
                userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_MODERATOR));
                logMessage = String.format(CHANGED_ROLE_SUCCESSFULLY, username, ROLE_MODERATOR);
                break;
            case ADMIN_CHANGE_ROLE:
                userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_USER));
                userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_MODERATOR));
                userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_ADMIN));
                logMessage = String.format(CHANGED_ROLE_SUCCESSFULLY, username, ROLE_ADMIN);
                break;
            default:
                logMessage = CHANGING_ROLE_FAILED;
                break;
        }

        userRepository.save(modelMapper.map(userServiceModel, User.class));

        logAction(adminUsername, logMessage);
    }

    @Override
    public void promote(String userId, String rootUsername) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        int nextPositionIndex = user.getPosition().ordinal() + 1;

        Position nextPosition = Position.values()[nextPositionIndex];
        user.setPosition(nextPosition);
        user.setCredits(user.getCredits() - MAX_CREDITS);
        user.setSalary(nextPosition.getSalary());

        userRepository.save(user);

        String logMessage = String.format(PROMOTED_SUCCESSFULLY, user.getUsername(), nextPosition.name());

        logAction(rootUsername, logMessage);
    }

    @Override
    public void demote(String userId, String rootUsername) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        int nextPositionIndex = user.getPosition().ordinal() - 1;

        Position nextPosition = Position.values()[nextPositionIndex];
        user.setPosition(nextPosition);
        user.setCredits(STARTING_CREDITS);
        user.setSalary(nextPosition.getSalary());

        userRepository.save(user);

        String logMessage = String.format(DEMOTED_SUCCESSFULLY, user.getUsername(), nextPosition.name());

        logAction(rootUsername, logMessage);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException((USERNAME_NOT_FOUND)));
    }

    private void defineUserRolesAndPosition(UserServiceModel userServiceModel) throws RoleNotFoundException {

        roleService.seedRoles();
        userServiceModel.setAuthorities(new HashSet<>());

        long repositoryCount = userRepository.count();

        if (repositoryCount == 0) {
            userServiceModel.setPosition(Position.PROJECT_MANAGER);
            userServiceModel.setSalary(Position.PROJECT_MANAGER.getSalary());
            userServiceModel.setCredits(MAX_CREDITS);
            userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_ROOT));
            userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_ADMIN));
            userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_MODERATOR));
            userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_USER));
        } else {
            userServiceModel.setPosition(Position.INTERN);
            userServiceModel.setSalary(Position.INTERN.getSalary());
            userServiceModel.setCredits(STARTING_CREDITS);
            userServiceModel.getAuthorities().add(roleService.findByAuthority(ROLE_USER));
        }
    }

    private void actualTaskComplete(User user, Task task) {

        long milliseconds = TimeUnit.MINUTES.toMillis(task.getMinutesNeeded());

        Thread thread = new MyThread(milliseconds, () ->{

            user.setCredits(user.getCredits() + task.getCredits());

            userRepository.save(user);

            task.setStatus(Status.FINISHED);

            taskRepository.save(task);
        });

        thread.start();
    }

    private String buildUpdatedEntityLogMessage(User user, UserServiceModel edited, boolean imageIsUpdated) {

        StringBuilder message = new StringBuilder();

        message.append(UPDATED_USER_SUCCESSFULLY)
                .append(System.lineSeparator());

        if (!user.getEmail().equals(edited.getEmail())) {
            message.append(UPDATED_USER_EMAIL)
                    .append(System.lineSeparator());
        }

        if (!encoder.matches(edited.getPassword(), user.getPassword())) {
            message.append(UPDATED_USER_PASSWORD)
                    .append(System.lineSeparator());
        }

        if (!user.getFirstName().equals(edited.getFirstName())) {
            message.append(UPDATED_USER_FIRST_NAME)
                    .append(System.lineSeparator());
        }

        if (!user.getLastName().equals(edited.getLastName())) {
            message.append(UPDATED_USER_LAST_NAME)
                    .append(System.lineSeparator());
        }

        if (imageIsUpdated) {
            message.append(UPDATED_USER_IMAGE)
                    .append(System.lineSeparator());
        }

        return message.toString();
    }

    private boolean updateImage(User user, UserServiceModel edited) throws IOException {

        if (edited.getImageUrl() != null) {

            if (user.getImageUrl() != null) {
                cloudinaryService.deleteImage(user.getImageId());
            }

            user.setImageUrl(edited.getImageUrl());
            user.setImageId(edited.getImageId());

            return true;
        }

        return false;
    }

    private void throwIfWrongOldPassword(String oldPassword, String password) {

        if (!encoder.matches(oldPassword, password)) {
            throw new WrongOldPasswordException(WRONG_OLD_PASSWORD_MESSAGE);
        }
    }

    private void throwIfUpdatedWithTakenEmail(String oldEmail, String newEmail) {

        if (!oldEmail.equals(newEmail)) {

            User userInDbWithSameEmail = userRepository.findByEmail(newEmail).orElse(null);

            if (userInDbWithSameEmail != null) {
                throw new EmailAlreadyExistException(DUPLICATE_USER_EMAIL_MESSAGE);
            }
        }
    }

    private void throwIfServiceModelNotValid(UserServiceModel userServiceModel) {
        if (!userValidationService.isValid(userServiceModel)) {
            throw new InvalidUserServiceModelException(INVALID_USER_SERVICE_MODEL_MESSAGE);
        }
    }

    private void logAction(String username, String description) {

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(username);
        logServiceModel.setCreatedOn(LocalDateTime.now());
        logServiceModel.setDescription(description);

        logService.create(logServiceModel);
    }

    private void throwIfUserExist(String username, String email) {

        User userInDb = userRepository.findByUsername(username).orElse(null);

        if (userInDb != null) {
            throw new UsernameAlreadyExistException(DUPLICATE_USER_USERNAME_MESSAGE);
        }

        User userInDbWithSameEmail = userRepository.findByEmail(email).orElse(null);

        if (userInDbWithSameEmail != null) {

            throw new EmailAlreadyExistException(DUPLICATE_USER_EMAIL_MESSAGE);
        }
    }
}
