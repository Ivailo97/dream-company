package dreamcompany.service.implementation;

import dreamcompany.common.GlobalConstants;
import dreamcompany.domain.entity.*;
import dreamcompany.domain.enumeration.Position;
import dreamcompany.domain.enumeration.Status;
import dreamcompany.domain.model.service.LogServiceModel;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.error.duplicates.TeamNameAlreadyExistException;
import dreamcompany.error.invalidservicemodels.InvalidTeamServiceModelException;
import dreamcompany.error.notexist.OfficeNotFoundException;
import dreamcompany.error.notexist.ProjectNotFoundException;
import dreamcompany.error.notexist.TeamNotFoundException;
import dreamcompany.error.notexist.UserNotFoundException;
import dreamcompany.repository.*;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.LogService;
import dreamcompany.service.interfaces.TeamService;
import dreamcompany.service.interfaces.validation.TeamValidationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dreamcompany.common.GlobalConstants.*;

@Service
@AllArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final TeamValidationService teamValidationService;

    private final CloudinaryService cloudinaryService;

    private final RoleRepository roleRepository;

    private final OfficeRepository officeRepository;

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final LogService logService;

    private final ModelMapper modelMapper;

    @Override
    public TeamServiceModel create(TeamServiceModel teamServiceModel, String moderatorUsername) {

        throwIfInvalidServiceModel(teamServiceModel);

        throwIfDuplicate(teamServiceModel);

        Team team = modelMapper.map(teamServiceModel, Team.class);

        // set office
        Office office = officeRepository.findById(teamServiceModel.getOffice().getId())
                .orElseThrow(() -> new OfficeNotFoundException(OFFICE_NOT_FOUND_MESSAGE));

        team.setOffice(office);

        //set profit
        team.setProfit(BigDecimal.ZERO);

        //set prev position
        List<UserServiceModel> employees = new ArrayList<>(teamServiceModel.getEmployees());

        User first = userRepository.findById(employees.get(0).getId()).orElse(null);

        team.setTeamLeaderPreviousPosition(first.getPosition());

        team = teamRepository.save(team);

        //set employees

        Team[] finalTeam = new Team[]{team};

        teamServiceModel.getEmployees().forEach(e -> {
            User employee = userRepository.findById(e.getId())
                    .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

            employee.setTeam(finalTeam[0]);
            userRepository.save(employee);
        });

        first.setPosition(Position.TEAM_LEADER);
        first.getAuthorities().add(roleRepository.findByAuthority(ROLE_MODERATOR).orElse(null));
        first.getAuthorities().add(roleRepository.findByAuthority(ROLE_ADMIN).orElse(null));
        first.setSalary(Position.TEAM_LEADER.getSalary());

        userRepository.save(first);

        //log action
        String employeesJoinedString = team.getEmployees()
                .stream()
                .map(e -> String.format("%s %s", e.getFirstName(), e.getLastName()))
                .collect(Collectors.joining(", "));

        String logMessage = String.format(CREATED_TEAM_SUCCESSFULLY, team.getName(), employeesJoinedString);

        logAction(moderatorUsername, logMessage);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    private void throwIfInvalidServiceModel(TeamServiceModel teamServiceModel) {

        if (!teamValidationService.isValid(teamServiceModel)) {
            throw new InvalidTeamServiceModelException(INVALID_TEAM_SERVICE_MODEL_MESSAGE);
        }
    }

    private void throwIfDuplicate(TeamServiceModel teamServiceModel) {

        Team teamInDb = teamRepository.findByName(teamServiceModel.getName()).orElse(null);

        if (teamInDb != null) {
            throw new TeamNameAlreadyExistException(GlobalConstants.DUPLICATE_TEAM_MESSAGE);
        }
    }

    @Override
    public TeamServiceModel edit(String id, TeamServiceModel edited, String moderatorUsername) throws IOException {

        throwIfInvalidEditedTeamServiceModel(edited);

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MESSAGE));

        if (!edited.getName().equals(team.getName())) {
            throwIfDuplicate(edited);
        }

        boolean logoIsUpdated = updateLogo(team, edited);

        Office office = officeRepository.findById(edited.getOffice().getId())
                .orElseThrow(() -> new OfficeNotFoundException(OFFICE_NOT_FOUND_MESSAGE));

        String logMessage = buildUpdatedEntityLogMessage(team, edited, logoIsUpdated);

        team.setName(edited.getName());

        team.setOffice(office);

        team = teamRepository.save(team);

        logAction(moderatorUsername, logMessage);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    private void throwIfInvalidEditedTeamServiceModel(TeamServiceModel edited) {

        if (edited == null || edited.getName() == null || edited.getOffice() == null) {
            throw new InvalidTeamServiceModelException(INVALID_TEAM_SERVICE_MODEL_MESSAGE);
        }
    }

    private boolean updateLogo(Team team, TeamServiceModel edited) throws IOException {

        if (edited.getLogoUrl() != null) {

            if (team.getLogoUrl() != null) {
                cloudinaryService.deleteImage(team.getLogoId());
            }

            team.setLogoUrl(edited.getLogoUrl());
            team.setLogoId(edited.getLogoId());

            return true;
        }

        return false;
    }

    @Override
    public TeamServiceModel delete(String id, String moderatorUsername) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MESSAGE));

        // setting previous position of the team leader
        User teamLeader = team.getEmployees().stream().filter(e -> e.getPosition() == Position.TEAM_LEADER)
                .findFirst().orElse(null);

        if (teamLeader != null) {
            teamLeader.setPosition(team.getTeamLeaderPreviousPosition());
            userRepository.save(teamLeader);
        }

        //removing the team from all users
        team.getEmployees().forEach(e -> {
            e.setTeam(null);
            userRepository.save(e);
        });

        Project assignedProject = team.getProject();

        if (assignedProject != null) {

            // removing the appointed employee from all tasks in the project
            assignedProject.getTasks().forEach(x -> {
                x.setEmployee(null);
                x.setStatus(Status.PENDING);
                taskRepository.save(x);
            });

            assignedProject.setStatus(Status.PENDING);
            projectRepository.save(assignedProject);
        }

        team.setProject(null);

        TeamServiceModel teamServiceModel = modelMapper.map(team, TeamServiceModel.class);

        cloudinaryService.deleteImage(team.getLogoId());
        teamRepository.delete(team);

        String logMessage = String.format(DELETED_TEAM_SUCCESSFULLY, team.getName());
        logAction(moderatorUsername, logMessage);

        return teamServiceModel;
    }

    @Override
    public TeamServiceModel findById(String id) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MESSAGE));

        return modelMapper.map(team, TeamServiceModel.class);
    }

    @Override
    public List<TeamServiceModel> findAll() {
        return teamRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TeamServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void assignProject(String projectId, String teamId, String managerUsername) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND_MESSAGE));

        project.setStatus(Status.IN_PROGRESS);

        projectRepository.save(project);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MESSAGE));

        team.setProject(project);

        teamRepository.save(team);

        String logMessage = String.format(ASSIGNED_PROJECT_SUCCESSFULLY, project.getName(), team.getName());

        logAction(managerUsername, logMessage);
    }

    @Override
    public List<TeamServiceModel> findAllWithoutProject() {

        return teamRepository.findAllByProjectNull()
                .stream()
                .map(p -> modelMapper.map(p, TeamServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addEmployeeToTeam(String teamId, String userId, String moderatorUsername) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MESSAGE));

        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        employee.setTeam(team);

        userRepository.save(employee);

        String logMessage = String.format(ADDED_EMPLOYEE_TO_TEAM_SUCCESSFULLY,
                String.format("%s %s", employee.getFirstName(), employee.getLastName()), team.getName());
        logAction(moderatorUsername, logMessage);
    }

    @Override
    public void removeEmployeeFromTeam(String teamId, String userId, String moderatorUsername) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(TEAM_NOT_FOUND_MESSAGE));

        if (team.getEmployees().size() > EMPLOYEES_MIN_COUNT) {

            User employee = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

            employee.setTeam(null);

            if (employee.getPosition() == Position.TEAM_LEADER) {

                employee.setPosition(team.getTeamLeaderPreviousPosition());
                employee.setSalary(team.getTeamLeaderPreviousPosition().getSalary());

                userRepository.save(employee);

                User nextLeader = team.getEmployees().toArray(User[]::new)[0];
                nextLeader.setPosition(Position.TEAM_LEADER);
                nextLeader.setSalary(Position.TEAM_LEADER.getSalary());

                userRepository.save(nextLeader);
            }

            String logMessage = String.format(REMOVED_EMPLOYEE_FROM_TEAM_SUCCESSFULLY,
                    String.format("%s %s", employee.getFirstName(), employee.getLastName()), team.getName());

            logAction(moderatorUsername, logMessage);
        }
    }

    private String buildUpdatedEntityLogMessage(Team team, TeamServiceModel edited, boolean logoIsUpdated) {

        StringBuilder message = new StringBuilder();

        message.append(String.format(UPDATED_TEAM_SUCCESSFULLY, team.getName()))
                .append(System.lineSeparator());

        if (!team.getName().equals(edited.getName())) {
            message.append(String.format(UPDATED_TEAM_NAME, edited.getName()))
                    .append(System.lineSeparator());
        }

        if (!team.getOffice().getId().equals(edited.getOffice().getId())) {
            message.append(UPDATED_TEAM_OFFICE)
                    .append(System.lineSeparator());
        }

        if (logoIsUpdated) {
            message.append(UPDATED_TEAM_LOGO)
                    .append(System.lineSeparator());
        }

        return message.toString();
    }

    private void logAction(String username, String description) {

        LogServiceModel logServiceModel = new LogServiceModel();
        logServiceModel.setUsername(username);
        logServiceModel.setCreatedOn(LocalDateTime.now());
        logServiceModel.setDescription(description);
        logService.create(logServiceModel);
    }
}
