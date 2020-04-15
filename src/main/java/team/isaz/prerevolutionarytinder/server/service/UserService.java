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


/**
 * Сервис управления пользователями
 */
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * <b>Регистрация пользователя.</b>
     *
     * @param username имя пользователя (уникальное)
     * @param password пароль
     * @param sex      пол (true=male)
     * @return {@link Response} о статусом true и UUID пользователя
     * в случае успешного создания и false с поясняющей строкой в противном.
     */
    public Response register(String username, String password, Boolean sex) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.debug("Пользователь с именем {} уже сущестует\nЕго данные:{}", username, user.toString());
            return new Response(false, "Пользователь с таким именем уже существует");
        }

        try {
            user = createUser(username, password, sex);
            userRepository.save(user);
        } catch (Throwable t) {
            logger.error(t.getMessage());
            return new Response(false, t.getMessage());
        }
        logger.debug("Новый пользователь успешно зарегистрирован. -->{}", user.toString());
        return new Response(true, user.getId());
    }

    /**
     * <b>Вход</b>
     *
     * @param username имя пользователя
     * @param password пароль
     * @return {@link Response} со статусом true и uuid пользователя если пароль совпал и false иначе.
     */
    public Response login(String username, String password) {
        var user = userRepository.findByUsername(username);
        if (user == null) return new Response(false, "Пользователя с таким именем не существует");
        if (BCrypt.checkpw(password, user.getPassword())) {
            return new Response(true, user.getId());
        }
        return new Response(false, "Неверный пароль");
    }

    /**
     * <b>Является ли администратором</b><br>
     * Проверка, является ли пользователь с данным id администратором
     *
     * @param userId id пользователя.
     * @return true, если является и false в остальных случаях.
     */
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

    /**
     * <b>Получить пользователя по id</b>
     *
     * @param id UUID пользователя
     * @return объект из репозитория с этим id или null
     */
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * <b>Генерация пользователя</b>
     *
     * @param name     имя
     * @param password пароль
     * @param sex      пол (true=male)
     * @return объект класса User с установленными параметрами.
     */
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

    /**
     * <b>Получить ID пользователей противоположного пола</b>
     *
     * @param id пользователей для которого ищем релевантные id
     * @return список uuid анкет
     */
    public List<UUID> getRelevantUsers(UUID id) {
        var user = userRepository.findById(id).orElseThrow();
        return userRepository.findAllBySex(!user.isSex())
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    /**
     * Генерация html-таблицы представления UserRepository
     *
     * @return Response содержащий таблицу.
     */
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

}
