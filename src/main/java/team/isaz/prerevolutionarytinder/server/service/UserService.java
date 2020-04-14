package team.isaz.prerevolutionarytinder.server.service;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.domain.entity.User;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Response register(String username, String password, Boolean sex) {
        User user = userRepository.findByUsername(username);
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

    public boolean isAdmin(UUID userId) {
        AtomicReference<Boolean> response = new AtomicReference<>(false);
        userRepository
                .findById(userId)
                .ifPresent(user ->
                        response.set(
                                user.getRoles() == 1
                        ));
        return response.get();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    private User createUser(String name, String password, Boolean sex) {
        name = name.trim();
        password = password.trim();
        User u = new User();
        u.setUsername(name);
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        u.setPassword(password);
        u.setRoles(0);
        u.setSex(sex);
        u.setProfileMessage("Пока здѣсь пусто...");
        return u;
    }

    public Response getTable() {
        var users = userRepository.findAll().iterator();

        var builder = new StringBuilder("<table>");
        builder.append("<tr>")
                .append("<th>ID</th>")
                .append("<th>USERNAME</th>")
                .append("<th>PWD</th>")
                .append("<th>ROLES</th>")
                .append("<th>SEX</th>")
                .append("<th>PROFILE_MESSAGE</th>")
                .append("</tr>");

        while (users.hasNext()) {
            var user = users.next();
            builder.append("<tr>");
            builder.append("<th>").append(user.getId()).append("</th>");
            builder.append("<th>").append(user.getUsername()).append("</th>");
            builder.append("<th>").append(user.getPassword()).append("</th>");
            builder.append("<th>").append(user.getRoles()).append("</th>");
            builder.append("<th>").append(user.isSex()).append("</th>");
            builder.append("<th>").append(user.getProfileMessage()).append("</th>");
            builder.append("</tr>");
        }

        builder.append("</table>");
        return new Response(true, builder.toString());
    }

    public List<UUID> getRelevantUsers(UUID id) {
        var user = userRepository.findById(id).orElseThrow();
        return userRepository.findAllBySex(!user.isSex())
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }


}
