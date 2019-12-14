package dreamcompany.web.interceptors;

import dreamcompany.domain.model.service.TeamServiceModel;
import dreamcompany.service.interfaces.TeamService;
import dreamcompany.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
@AllArgsConstructor
public class EditTeamEmployeesInterceptor extends HandlerInterceptorAdapter {

    private final TeamService teamService;

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        HttpSession session = request.getSession();
        session.setAttribute("canAddEmployee", userService.findAllWithoutTeam().size() > 0);

        String url = request.getRequestURL().toString();
        String teamId = url.substring(url.lastIndexOf('/') + 1);
        TeamServiceModel teamServiceModel = teamService.findById(teamId);
        session.setAttribute("canRemoveEmployee", teamServiceModel.getEmployees().size() > 2);
    }
}
