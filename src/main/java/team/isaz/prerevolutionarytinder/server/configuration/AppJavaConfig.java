package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryConfig.class)
public class AppJavaConfig {
    RepositoryConfig repositories;

    @Autowired
    AppJavaConfig(RepositoryConfig repositories) {
        this.repositories = repositories;
    }

    /*
    @Bean
    public UserService userService() {
        return new UserService(
                repositories.userRepository(),
                repositories.roleRepository(),
                repositories.likeRepository()
        );
    }*/


}
