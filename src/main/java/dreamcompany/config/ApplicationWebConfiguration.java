package dreamcompany.config;

import dreamcompany.web.interceptors.LeaderInterceptor;
import dreamcompany.web.interceptors.FaviconInterceptor;
import dreamcompany.web.interceptors.ManagerInterceptor;
import dreamcompany.web.interceptors.ProfilePictureInterceptor;
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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(faviconInterceptor);
        registry.addInterceptor(leaderInterceptor);
        registry.addInterceptor(managerInterceptor);
        registry.addInterceptor(profilePictureInterceptor);
    }
}
