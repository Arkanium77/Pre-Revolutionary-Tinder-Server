package team.isaz.prerevolutionarytinder.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.domain.Session;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * <b>Сервис управления подключениями</b><br>
 * Хранит данные о подключениях и удаляет их по истечении времени подключения.
 */
public class SessionService {
    final int MAX_MINUTE_SESSION_LIFETIME = 1;
    Logger logger = LoggerFactory.getLogger(SessionService.class);
    Map<UUID, Session> activeSessions;

    public SessionService() {
        activeSessions = new HashMap<>();
    }

    /**
     * <b>Регистрация сессии</b><br>
     *
     * @param userId ID подключающегося пользователя.
     * @return {@link Response} с UUID созданной сессии.
     */
    public Response registerSession(UUID userId) {
        var session = new Session(userId);
        var sessionId = genUniq();
        activeSessions.put(sessionId, session);
        logger.debug("Для пользователя {} зарегистрирована сессия {}", sessionId, userId);
        return new Response(true, sessionId);
    }

    /**
     * <b>Генератор уникального UUID для сессии</b>
     *
     * @return UUID сессии, гарантированно не пересекающийся с уже созданными.
     */
    private UUID genUniq() {
        var id = UUID.randomUUID();
        while (activeSessions.get(id) != null) {
            id = UUID.randomUUID();
        }
        return id;
    }

    /**
     * <b>Проверка состояния сессии</b>
     *
     * @param sessionId ID сессии
     * @param time      текущее время
     * @return {@link Response} с true, в случае, если сессия существет меньшее,
     * чем {@link SessionService#MAX_MINUTE_SESSION_LIFETIME} время и false в остальных случаях.
     */
    public Response getSessionActivity(UUID sessionId, LocalDateTime time) {
        if (!activeSessions.containsKey(sessionId)) {
            return new Response(false, "О нѣтъ! Такой сессии не существует.");
        }
        var session = activeSessions.get(sessionId);
        if (isSessionInactive(session, time)) {
            activeSessions.remove(sessionId);
            return new Response(false, "О нѣтъ! Время вышло! Войдите заново.");
        }
        return new Response(true, session.getUserId());
    }

    /**
     * <b>Проверка несоответствия сессии лайфтайму.</b>
     *
     * @param session проверяемая сессмя
     * @param time    текущее время.
     * @return true, если время с момента создания сесии больше, чем {@link SessionService#MAX_MINUTE_SESSION_LIFETIME}
     */
    private boolean isSessionInactive(Session session, LocalDateTime time) {
        var lifetime = ChronoUnit.MINUTES.between(session.getCreateTime(), time);
        return lifetime > MAX_MINUTE_SESSION_LIFETIME;
    }

    /**
     * <b>Получить все активные сессии</b>
     *
     * @param time текущее время.
     * @return Set<UUID> содержащий ID всех активных сессий.
     */
    public Set<UUID> getActiveSessions(LocalDateTime time) {
        clearAllInactiveSession(time);
        return activeSessions.keySet();
    }

    /**
     * Очистить список сессий от несоответствующих лайфтайму.
     *
     * @param time текущее время.
     */
    public void clearAllInactiveSession(LocalDateTime time) {
        var inactive = activeSessions
                .keySet()
                .stream()
                .filter(uuid -> isSessionInactive(activeSessions.get(uuid), time))
                .collect(Collectors.toSet());
        logger.debug(getLoggingStringForInactiveSessions(inactive));
        inactive.forEach(activeSessions::remove);
    }

    private String getLoggingStringForInactiveSessions(Set<UUID> inactive) {
        if (inactive.size() == 0) return "Неактивных сессий не обнаружено";
        StringBuilder loggingString = new StringBuilder("Обнаружены " + inactive.size() + " неактивных сессий:");
        Integer i = 1;
        for (UUID uuid : inactive) {
            loggingString
                    .append("\n")
                    .append('\t')
                    .append(i)
                    .append(")")
                    .append(uuid);
            i++;
        }
        return loggingString.toString();
    }
}
