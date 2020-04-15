package team.isaz.prerevolutionarytinder.server.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

class SessionServiceTest {
    SessionService sessionService;

    @BeforeEach
    public void before() {
        sessionService = new SessionService();
    }

    @Test
    void registerSession() {
        UUID uuid = UUID.randomUUID();
        Response r = sessionService.registerSession(uuid);

        Assertions.assertThat(r.isStatus()).isTrue();

        Assertions
                .assertThat(
                        (UUID) sessionService.getSessionActivity(
                                (UUID) r.getAttach(), LocalDateTime.now())
                                .getAttach())
                .isEqualByComparingTo(uuid);
    }

    @Test
    void isSessionActive() {
        UUID uuid = UUID.randomUUID();
        var r = sessionService.registerSession(uuid);

        Assertions
                .assertThat(r.isStatus())
                .isTrue();

        Assertions
                .assertThat(sessionService.getSessionActivity((UUID) r.getAttach(), LocalDateTime.now()).isStatus())
                .isTrue();

        Assertions
                .assertThat(sessionService.getSessionActivity((UUID) r.getAttach(), LocalDateTime.MAX).isStatus())
                .isFalse();
    }

    @Test
    void idOfActiveSessions() {
        sessionService.registerSession(UUID.randomUUID());
        sessionService.registerSession(UUID.randomUUID());
        sessionService.registerSession(UUID.randomUUID());
        var r = sessionService.getActiveSessions(LocalDateTime.now());

        Assertions
                .assertThat(r.size())
                .isEqualTo(sessionService.activeSessions.size());

        Assertions
                .assertThat(r)
                .isEqualTo(sessionService.activeSessions.keySet());

        r = sessionService.getActiveSessions(LocalDateTime.MAX);
        Assertions
                .assertThat(r.size())
                .isEqualTo(sessionService.activeSessions.size())
                .isEqualTo(0);

        Assertions
                .assertThat(r)
                .isEqualTo(sessionService.activeSessions.keySet())
                .isEqualTo(new HashSet<>());

    }


}