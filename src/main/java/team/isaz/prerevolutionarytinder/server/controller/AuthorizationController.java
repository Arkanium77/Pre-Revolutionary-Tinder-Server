package team.isaz.prerevolutionarytinder.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {
    private UserService userService;
    private SessionService sessionService;

    public AuthorizationController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> params) {
        var username = params.get("username");
        var password = params.get("password");
        if (username == null || password == null)
            return new ResponseEntity<>("Incorrect request body. <br> Expected: JSON, {\"username\":x, \"password\":y}.", HttpStatus.BAD_REQUEST);
        var response = userService.login(username, password);
        return getSessionCreateResponseEntity(response);
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> params) {
        var username = params.get("username");
        var password = params.get("password");
        var sex = Boolean.parseBoolean(params.get("sex"));
        if (username == null || password == null)
            return new ResponseEntity<>(
                    "Incorrect request body. <br> Expected: JSON, {\"username\":x, \"password\":y, \"sex\":z}.",
                    HttpStatus.BAD_REQUEST);

        var response = userService.register(username, password, sex);
        return getSessionCreateResponseEntity(response);
    }

    private ResponseEntity<String> getSessionCreateResponseEntity(Response response) {
        if (!response.isStatus()) return new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);

        UUID userId = (UUID) response.getAttach();
        var sessionResponse = sessionService.registerSession(userId);

        return sessionResponse.isStatus()
                ? new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }
}
