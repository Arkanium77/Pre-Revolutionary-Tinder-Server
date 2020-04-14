package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import team.isaz.prerevolutionarytinder.server.domain.repository.LikeRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

@Configuration
public class RepositoryJavaConfig {
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Autowired
    public RepositoryJavaConfig(UserRepository userRepository,
                                LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public UserRepository userRepository() {
        return this.userRepository;
    }

    public LikeRepository likeRepository() {
        return this.likeRepository;
    }
}

