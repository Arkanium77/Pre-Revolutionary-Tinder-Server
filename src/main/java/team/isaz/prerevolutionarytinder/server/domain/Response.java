package team.isaz.prerevolutionarytinder.server.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Response {
    boolean status;
    Object attach;

    public Response() {
        status = false;
    }

    public Response(boolean status, Object attach) {
        this.status = status;
        this.attach = attach;
    }

}
