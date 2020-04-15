package team.isaz.prerevolutionarytinder.server.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>Сессия</b><br>
 * Сущность содержит данные о подключении, такие как UUID пользователя и DateTime подключения.<br>
 * В дальнейшем, на основе этих данных {@link team.isaz.prerevolutionarytinder.server.service.SessionService SessionService}
 * принимает решение об отключении пользователя.
 */
@Getter
@EqualsAndHashCode
public class Session {
    private LocalDateTime createTime;
    private UUID userId;

    private Session() {
    }

    public Session(UUID id) {
        createTime = LocalDateTime.now();
        userId = id;
    }

    public Session(UUID id, LocalDateTime dateTime) {
        createTime = dateTime;
        userId = id;
    }
}
