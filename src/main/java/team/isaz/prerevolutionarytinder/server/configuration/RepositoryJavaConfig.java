package team.isaz.prerevolutionarytinder.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import team.isaz.prerevolutionarytinder.server.domain.repository.LikeRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.RoleRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

@Configuration
public class RepositoryJavaConfig {
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public RepositoryJavaConfig(UserRepository userRepository,
                                LikeRepository likeRepository,
                                RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.roleRepository = roleRepository;
    }

    public UserRepository userRepository() {
        return this.userRepository;
    }

    public LikeRepository likeRepository() {
        return this.likeRepository;
    }

    public RoleRepository roleRepository() {
        return this.roleRepository;
    }
}

