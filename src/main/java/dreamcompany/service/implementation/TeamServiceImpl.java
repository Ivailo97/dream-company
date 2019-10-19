package dreamcompany.service.implementation;

import dreamcompany.domain.entity.*;
import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.repository.OfficeRepository;
import dreamcompany.repository.ProjectRepository;
import dreamcompany.repository.TeamRepository;
import dreamcompany.repository.UserRepository;
import dreamcompany.service.interfaces.TeamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    private final OfficeRepository officeRepository;

    private final ProjectRepository projectRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, OfficeRepository officeRepository, ProjectRepository projectRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.officeRepository = officeRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TeamServiceModel create(TeamServiceModel teamServiceModel) {

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
        first.setSalary(Position.TEAM_LEADER.getSalary());

        userRepository.save(first);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    @Override
    public TeamServiceModel edit(String id, TeamServiceModel teamServiceModel) {

        Team team = this.teamRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid team id"));

        team.setName(teamServiceModel.getName());

        // set new office
        Office office = officeRepository.findById(teamServiceModel.getOffice().getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid office id"));

        team.setOffice(office);

        team = teamRepository.save(team);

        return modelMapper.map(team, TeamServiceModel.class);
    }

    @Override
    public TeamServiceModel delete(String id) {

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

        team.setProject(null);

        TeamServiceModel teamServiceModel = modelMapper.map(team, TeamServiceModel.class);
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

        if (team.getEmployees().size() > 2){

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
