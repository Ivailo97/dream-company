package dreamcompany.config;

import dreamcompany.web.interceptors.CheckLeaderInterceptor;
import dreamcompany.web.interceptors.FaviconInterceptor;
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

    private final CheckLeaderInterceptor checkLeaderInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(faviconInterceptor);
        registry.addInterceptor(checkLeaderInterceptor);
    }
}
