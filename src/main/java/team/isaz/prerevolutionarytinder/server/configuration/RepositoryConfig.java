package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

@Configuration
public class RepositoryConfig {
    private final UserRepository userRepository;

    @Autowired
    public RepositoryConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository userRepository() {
        return userRepository;
    }

}
