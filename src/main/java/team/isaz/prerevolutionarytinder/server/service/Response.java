package team.isaz.prerevolutionarytinder.server.service;

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
