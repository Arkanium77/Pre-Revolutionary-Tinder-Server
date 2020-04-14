package team.isaz.prerevolutionarytinder.server.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

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
}
