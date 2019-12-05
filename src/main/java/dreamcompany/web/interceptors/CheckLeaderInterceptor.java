package dreamcompany.web.interceptors;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Component
@AllArgsConstructor
public class CheckLeaderInterceptor extends HandlerInterceptorAdapter {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        Principal user = request.getUserPrincipal();


        if (user != null) {
            UserServiceModel loggedUser = userService.findByUsername(user.getName());
            boolean isTeamLeader = loggedUser.getPosition() == Position.TEAM_LEADER;
            boolean isTeamLeaderWithAssignedProject = userService.isLeaderWithAssignedProject(user.getName());
            request.getSession().setAttribute("isTeamLeader", isTeamLeader);

            if (isTeamLeader) {
                request.getSession().setAttribute("teamId", loggedUser.getTeam().getId());
            }

            request.getSession().setAttribute("isLeaderWithAssignedProject", isTeamLeaderWithAssignedProject);

            if (isTeamLeaderWithAssignedProject) {
                request.getSession().setAttribute("projectId", loggedUser.getTeam().getProject().getId());
            }

        } else {
            request.getSession().setAttribute("isTeamLeader", false);
            request.getSession().setAttribute("isLeaderWithAssignedProject", false);
        }
    }
}
