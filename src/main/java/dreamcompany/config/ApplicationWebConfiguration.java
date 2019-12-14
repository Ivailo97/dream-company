package dreamcompany.config;

import dreamcompany.web.interceptors.*;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class ApplicationWebConfiguration implements WebMvcConfigurer {

    private final FaviconInterceptor faviconInterceptor;

    private final LeaderInterceptor leaderInterceptor;

    private final ManagerInterceptor managerInterceptor;

    private final ProfilePictureInterceptor profilePictureInterceptor;

    private final CreateTaskInterceptor createTaskInterceptor;

    private final CreateTeamInterceptor createTeamInterceptor;

    private final EditTeamEmployeesInterceptor editTeamEmployeesInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(faviconInterceptor);
        registry.addInterceptor(leaderInterceptor);
        registry.addInterceptor(managerInterceptor);
        registry.addInterceptor(profilePictureInterceptor);
        registry.addInterceptor(createTaskInterceptor)
                .addPathPatterns("/create","/teams/create","/offices/create","/tasks/create","/projects/create");
        registry.addInterceptor(createTeamInterceptor)
                .addPathPatterns("/create","/teams/create","/offices/create","/tasks/create","/projects/create");
        registry.addInterceptor(editTeamEmployeesInterceptor)
                .addPathPatterns("/teams/edit/**");
    }
}
