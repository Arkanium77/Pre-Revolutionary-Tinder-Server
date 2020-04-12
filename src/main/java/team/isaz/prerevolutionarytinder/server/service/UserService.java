package team.isaz.prerevolutionarytinder.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import team.isaz.prerevolutionarytinder.server.domain.entities.Role;
import team.isaz.prerevolutionarytinder.server.domain.entities.User;
import team.isaz.prerevolutionarytinder.server.domain.repository.LikeRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.RoleRepository;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

import java.util.HashSet;

@Service
public class UserService implements UserDetailsService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;
    RoleRepository roleRepository;
    LikeRepository likeRepository;
    BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.likeRepository = likeRepository;
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(s);

        if (user == null) {
            logger.info("User {} is not exist", s);
        }

        return user;
    }

    public Response register(String username, String password, boolean sex) {
        var user = userRepository.findByUsername(username);
        if (user != null) {
            return new Response(false, "Пользователь с таким именем уже существует");
        }

        try {
            user = createUser(username, password, sex);
            userRepository.save(user);
        } catch (Throwable t) {
            logger.error(t.getMessage());
            return new Response(false, t.getMessage());
        }
        return new Response(true, user.getId());
    }

    public Response login(String username, String password) {
        var user = userRepository.findByUsername(username);
        if (user == null) return new Response(false, "Пользователя с таким именем не существует");
        if (BCrypt.checkpw(password, user.getPassword())) {
            return new Response(true, user.getId());
        }
        return new Response(false, "Неверный пароль");
    }

    private User createUser(String name, String password, boolean sex) {
        name = name.trim();
        password = password.trim();
        User u = new User();
        u.setUsername(name);
        password = encoder.encode(password);
        u.setPassword(password);
        u.setRoles(new HashSet<>());
        u.getRoles().add(new Role());
        u.setSex(sex);
        u.setProfile_message("Пока здѣсь пусто...");
        return u;
    }
}
