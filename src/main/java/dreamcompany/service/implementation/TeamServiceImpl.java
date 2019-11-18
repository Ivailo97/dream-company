package dreamcompany.service.implementation;

import dreamcompany.GlobalConstraints;
import dreamcompany.domain.entity.*;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.error.duplicates.TeamNameAlreadyExistException;
import dreamcompany.repository.*;
import dreamcompany.service.interfaces.CloudinaryService;
import dreamcompany.service.interfaces.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static dreamcompany.GlobalConstraints.ROLE_ADMIN;
import static dreamcompany.GlobalConstraints.ROLE_MODERATOR;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final CloudinaryService cloudinaryService;

    private final RoleRepository roleRepository;

    private final OfficeRepository officeRepository;

    private final ProjectRepository projectRepository;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, CloudinaryService cloudinaryService, RoleRepository roleRepository, OfficeRepository officeRepository, ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.cloudinaryService = cloudinaryService;
        this.roleRepository = roleRepository;
        this.officeRepository = officeRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TeamServiceModel create(TeamServiceModel teamServiceModel) {

        throwIfDuplicate(teamServiceModel);

        Team team = modelMapper.map(teamServiceModel, Team.class);

        // set office
        Office office = officeRepository.findById(teamServiceModel.getOffice().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid office id"));

        team.setOffice(office);

        //set profit
        team.setProfit(BigDecimal.ZERO);

        // set prev position
        List<UserServiceModel> employees = new ArrayList<>(teamServiceModel.getEmployees());

        User first = userRepository.findById(employees.get(0).getId()).orElse(null);

        team.setTeamLeaderPreviousPosition(first.getPosition());

        team = teamRepository.save(team);

        //set employees

        Team[] finalTeam = new Team[]{team};

        teamServiceModel.getEmployees().forEach(e -> {
            User employee = userRepository.findById(e.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

            employee.setTeam(finalTeam[0]);
            userRepository.save(employee);
        });

        first.setPosition(Position.TEAM_LEADER);
        first.getAuthorities().add(roleRepository.findByAuthority(ROLE_MODERATOR).orElse(null));
        first.getAuthorities().add(roleRepository.findByAuthority(ROLE_ADMIN).orElse(null));
        first.setSalary(Position.TEAM_LEADER.getSalary());

        userRepository.save(first);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    private void throwIfDuplicate(TeamServiceModel teamServiceModel) {

        Team teamInDb = teamRepository.findByName(teamServiceModel.getName()).orElse(null);

        if (teamInDb != null) {
            throw new TeamNameAlreadyExistException(GlobalConstraints.DUPLICATE_TEAM_MESSAGE);
        }
    }

    @Override
    public TeamServiceModel edit(String id, TeamServiceModel edited) throws IOException {

        Team team = this.teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

        if (!edited.getName().equals(team.getName())) {
            throwIfDuplicate(edited);
        }

        boolean logoIsUpdated = updateLogo(team, edited);

        team.setName(edited.getName());

        // set new office
        Office office = officeRepository.findById(edited.getOffice().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid office id"));

        team.setOffice(office);

        team = teamRepository.save(team);

        return modelMapper.map(team, TeamServiceModel.class);
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
    public TeamServiceModel delete(String id) throws IOException {

        Team team = this.teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

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
        return teamServiceModel;
    }

    @Override
    public TeamServiceModel findById(String id) {

        Team team = this.teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

        return modelMapper.map(team, TeamServiceModel.class);
    }

    @Override
    public List<TeamServiceModel> findAll() {
        return teamRepository.findAll().stream()
                .map(t -> modelMapper.map(t, TeamServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void assignProject(String projectId, String teamId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project id"));

        project.setStatus(Status.IN_PROGRESS);

        projectRepository.save(project);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

        team.setProject(project);

        teamRepository.save(team);
    }

    @Override
    public List<TeamServiceModel> findAllWithoutProject() {

        return teamRepository.findAllByProjectNull()
                .stream()
                .map(p -> modelMapper.map(p, TeamServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void addEmployeeToTeam(String teamId, String userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

        User employee = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        employee.setTeam(team);

        userRepository.save(employee);
    }

    @Override
    public void removeEmployeeFromTeam(String teamId, String userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

        if (team.getEmployees().size() > 2) {

            User employee = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

            if (employee.getPosition() == Position.TEAM_LEADER) {
                employee.setPosition(team.getTeamLeaderPreviousPosition());
                employee.setSalary(team.getTeamLeaderPreviousPosition().getSalary());
                User nextLeader = team.getEmployees().toArray(User[]::new)[0];
                nextLeader.setPosition(Position.TEAM_LEADER);
                nextLeader.setSalary(Position.TEAM_LEADER.getSalary());
                userRepository.save(nextLeader);
            }

            employee.setTeam(null);

            userRepository.save(employee);
        }
    }
}
