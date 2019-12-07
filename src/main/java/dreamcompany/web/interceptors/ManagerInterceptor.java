package dreamcompany.web.interceptors;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.Status;
import dreamcompany.domain.model.service.UserServiceModel;
import dreamcompany.service.interfaces.ProjectService;
import dreamcompany.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Component
@AllArgsConstructor
public class ManagerInterceptor extends HandlerInterceptorAdapter {

    private final UserService userService;

    private final ProjectService projectService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

        Principal user = request.getUserPrincipal();

        HttpSession session = request.getSession();

        if (user != null) {

            UserServiceModel loggedUser = userService.findByUsername(user.getName());

            if (loggedUser.getPosition() == Position.PROJECT_MANAGER) {
                session.setAttribute("hasProjects", projectService.findAllByStatus(Status.PENDING.name()).size() > 0);
            }

        } else {
            session.setAttribute("hasProjects", false);
        }
    }
}
