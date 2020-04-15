package team.isaz.prerevolutionarytinder.server.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * <b>Response</b>
 * Сущность, используемая для передачи данных между сервисами.
 * Содержит статус операции и, опционально, дополнительные данные.
 * Это может быть строка-пояснение, данные необходимые для работы программы или какой-то мета-контейнер.
 */
@Getter
@EqualsAndHashCode
public class Response {
    boolean status;
    Object attach;

    public Response() {
        status = false;
    }

    public Response(boolean status) {
        this.status = status;
        this.attach = null;
    }

    public Response(boolean status, Object attach) {
        this.status = status;
        this.attach = attach;
    }

}
