package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import team.isaz.prerevolutionarytinder.server.service.UserService;

@Configuration
@EnableJpaRepositories("team.isaz.prerevolutionarytinder.server.domain.repository")
public class AppJavaConfig {
    @Bean
    public UserService userService() {
        return new UserService();
    }
    /*
    @Bean
    public UserService userService(){
        return new UserService(userRepository());
    }

    @Bean
    public UserRepository userRepository(){
        return new
    }
    */

}
