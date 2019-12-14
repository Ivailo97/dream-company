package dreamcompany.web.interceptors;

import dreamcompany.service.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class CreateTeamInterceptor extends HandlerInterceptorAdapter {

    private final UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        request.getSession().setAttribute("canCreateTeam", userService.findAllWithoutTeam().size() > 1);
    }
}
