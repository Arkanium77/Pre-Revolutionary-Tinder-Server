package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import team.isaz.prerevolutionarytinder.server.service.LoginService;

@Configuration
public class AppJavaConfig {
    @Bean
    public LoginService userService() {
        return new LoginService();
    }



}
