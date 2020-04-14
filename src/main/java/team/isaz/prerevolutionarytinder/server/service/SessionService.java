package team.isaz.prerevolutionarytinder.server.service;

import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.domain.Session;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class SessionService {
    final int MAX_MINUTE_SESSION_LIFETIME = 10;
    Map<UUID, Session> activeSessions;

    public SessionService() {
        activeSessions = new HashMap<>();
    }

    public Response registerSession(UUID userId) {
        var session = new Session(userId);
        var sessionId = genUniq();
        activeSessions.put(sessionId, session);
        return new Response(true, sessionId);
    }

    private UUID genUniq() {
        var id = UUID.randomUUID();
        while (activeSessions.get(id) != null) {
            id = UUID.randomUUID();
        }
        return id;
    }

    public Response isSessionActive(UUID sessionId) {
        if (!activeSessions.containsKey(sessionId)) {
            return new Response(false, "О нѣтъ! Такой сессии не существует.");
        }
        var session = activeSessions.get(sessionId);
        var check = check(session);
        if (!check) {
            activeSessions.remove(sessionId);
            return new Response(check, "О нѣтъ! Время вышло! Войдите заново.");
        }
        return new Response(check, session.getUserId());
    }

    private boolean check(Session session) {
        var lifetime = ChronoUnit.MINUTES.between(session.getCreateTime(), LocalDateTime.now());
        return lifetime < MAX_MINUTE_SESSION_LIFETIME;
    }

    public Set<UUID> getActiveSessions() {
        return activeSessions.keySet();
    }
}
