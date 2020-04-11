package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import team.isaz.prerevolutionarytinder.server.service.LoginService;

@Configuration
@Import(RepositoryConfig.class)
public class AppJavaConfig {
    RepositoryConfig repositories;

    @Autowired
    AppJavaConfig(RepositoryConfig repositories) {
        this.repositories = repositories;
    }

    @Bean
    public LoginService userService() {
        return new LoginService(repositories.userRepository());
    }


}
