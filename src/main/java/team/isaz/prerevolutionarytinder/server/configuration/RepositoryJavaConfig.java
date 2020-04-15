package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import team.isaz.prerevolutionarytinder.server.domain.repository.RelationRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

@Configuration
public class RepositoryJavaConfig {
    private final UserRepository userRepository;
    private final RelationRepository relationRepository;

    @Autowired
    public RepositoryJavaConfig(UserRepository userRepository,
                                RelationRepository relationRepository) {
        this.userRepository = userRepository;
        this.relationRepository = relationRepository;
    }

    public UserRepository userRepository() {
        return this.userRepository;
    }

    public RelationRepository likeRepository() {
        return this.relationRepository;
    }
}

