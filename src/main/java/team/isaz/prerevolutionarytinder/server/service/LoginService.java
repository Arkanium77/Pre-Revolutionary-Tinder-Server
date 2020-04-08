package team.isaz.prerevolutionarytinder.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import team.isaz.prerevolutionarytinder.server.domain.entities.User;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

public class LoginService {
    Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    UserRepository userRepository;
    //TODO: Репозиторий не через Autowired

    public Response register(String username, String password, boolean sex) {
        User user;
        if (userRepository.findByUsername(username) != null) {
            return new Response(false, "Пользователя с таким именем не существует");
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

    public void insertSample() {
        User u = createUser("Милая Проказница", "1", false);
        userRepository.save(u);
        u = createUser("Бѣдная Лиза", "1", false);
        userRepository.save(u);
        u = createUser("Состоятельная особа", "1", false);
        userRepository.save(u);
        u = createUser("Мстительный авантюрист", "1", true);
        userRepository.save(u);
        u = createUser("Загадочный Петръ", "1", true);
        userRepository.save(u);
    }


    private User createUser(String name, String password, boolean sex) {
        name = name.trim();
        password = password.trim();
        User u = new User();
        u.setUsername(name);
        password = encodePassword(password);
        u.setPassword(password);
        u.setRole(0);
        u.setSex(sex);
        u.setProfile_message("Пока здѣсь пусто...");
        return u;
    }

    private String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
