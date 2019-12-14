package dreamcompany.web.interceptors;

import dreamcompany.domain.enumeration.Position;
import dreamcompany.domain.model.service.TaskServiceModel;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Set;

@Component
@AllArgsConstructor
public class LeaderInterceptor extends HandlerInterceptorAdapter {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        Principal user = request.getUserPrincipal();

        if (user != null) {

            UserServiceModel loggedUser = userService.findByUsername(user.getName());
            boolean hasTeam = loggedUser.getTeam() != null;
            boolean isTeamLeader = loggedUser.getPosition() == Position.TEAM_LEADER;
            boolean isTeamLeaderWithAssignedProject = userService.isLeaderWithAssignedProject(user.getName());
            request.getSession().setAttribute("hasTeam", hasTeam);


            if (hasTeam) {
                request.getSession().setAttribute("teamId", loggedUser.getTeam().getId());
            }

            request.getSession().setAttribute("isTeamLeader", isTeamLeader);
            request.getSession().setAttribute("isLeaderWithAssignedProject", isTeamLeaderWithAssignedProject);

            if (isTeamLeaderWithAssignedProject) {
                request.getSession().setAttribute("projectId", loggedUser.getTeam().getProject().getId());
                Set<TaskServiceModel> tasks = loggedUser.getTeam().getProject().getTasks();
                request.getSession().setAttribute("projectHaveTasks", tasks != null && tasks.size() > 0);
            }

        } else {
            request.getSession().setAttribute("isTeamLeader", false);
            request.getSession().setAttribute("isLeaderWithAssignedProject", false);
        }
    }
}
