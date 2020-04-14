package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import team.isaz.prerevolutionarytinder.server.service.CashOfRelated;
import team.isaz.prerevolutionarytinder.server.service.LikeService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;

@Configuration
@Import(RepositoryJavaConfig.class)
public class AppJavaConfig {
    RepositoryJavaConfig repositories;

    @Autowired
    AppJavaConfig(RepositoryJavaConfig repositories) {
        this.repositories = repositories;
    }

    @Bean
    public SessionService sessionService() {
        return new SessionService();
    }

    @Bean
    public UserService userService() {
        return new UserService(repositories.userRepository());
    }

    @Bean
    public LikeService likeService() {
        return new LikeService(repositories.likeRepository());
    }

    @Bean
    public CashOfRelated cashOfRelated() {
        return new CashOfRelated(userService(), likeService());
    }

}
