package dreamcompany.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class BeanConfiguration {

    private static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
    }

    @Bean
    public ModelMapper modelMapper() {
        return mapper;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AtomicInteger atomicInteger(){
        return new AtomicInteger();
    }
}
