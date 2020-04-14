package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RepositoryJavaConfig.class)
public class AppJavaConfig {
    RepositoryJavaConfig repositories;

    @Autowired
    AppJavaConfig(RepositoryJavaConfig repositories) {
        this.repositories = repositories;
    }

}
