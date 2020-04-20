package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import team.isaz.prerevolutionarytinder.server.service.CacheOfRelated;
import team.isaz.prerevolutionarytinder.server.service.RelationService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;

@Configuration
@Import(RepositoryJavaConfig.class)
public class AppJavaConfig {
    private final RepositoryJavaConfig repositories;

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
    public RelationService likeService() {
        return new RelationService(repositories.likeRepository());
    }

    @Bean
    public CacheOfRelated cashOfRelated() {
        return new CacheOfRelated(userService(), likeService());
    }

}
