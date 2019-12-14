package dreamcompany.web.interceptors;

import dreamcompany.service.interfaces.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class CreateTaskInterceptor extends HandlerInterceptorAdapter {

    private final ProjectService projectService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
        request.getSession().setAttribute("canCreateTask", projectService.findAll().size() > 0);
    }
}
