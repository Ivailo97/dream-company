package dreamcompany.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .disable()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/js/**","/img/**")
                .permitAll()
                .antMatchers("/", "/users/login", "/users/register")
                .anonymous()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/home")
                .and()
                .logout()
                .logoutUrl("/users/logout");
        //  .logoutSuccessUrl("/");
    }
}
