package team.isaz.prerevolutionarytinder.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;
import team.isaz.prerevolutionarytinder.server.utils.MappingUtils;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/authorization")
public class AuthorizationController {
    Logger logger = LoggerFactory.getLogger(AuthorizationController.class);
    private UserService userService;
    private SessionService sessionService;

    public AuthorizationController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> params) {
        var username = params.get("username");
        var password = params.get("password");
        logger.debug("Попытка авторизации\n\tusername: {}\n\tpassword: {}", username, password);
        if (username == null || password == null)
            return new ResponseEntity<>("Incorrect request body. <br> Expected: JSON, {\"username\":x, \"password\":y}.", HttpStatus.BAD_REQUEST);
        var response = userService.login(username, password);
        logger.debug("Ответ авторизации: {}:\t{}", response.isStatus(), response.getAttach());
        return getSessionCreateResponseEntity(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> params) {
        var username = params.get("username");
        var password = params.get("password");
        var sex = MappingUtils.booleanMapping(params.get("sex"));
        logger.debug("Попытка регистрации\n\tusername: {}\n\tpassword: {}\n\tsex: {}", username, password, sex.getAttach());
        if (username == null || password == null || !sex.isStatus())
            return new ResponseEntity<>(
                    "Incorrect request body. <br> Expected: JSON, {\"username\":x, \"password\":y, \"sex\":z}.",
                    HttpStatus.BAD_REQUEST);

        var response = userService.register(username, password, (Boolean) sex.getAttach());
        logger.debug("Ответ регистрации: {}:\t{}", response.isStatus(), response.getAttach());
        return getSessionCreateResponseEntity(response);
    }

    private ResponseEntity<String> getSessionCreateResponseEntity(Response response) {
        if (!response.isStatus()) return new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);
        logger.debug("Response: Status = {}; Attach: {}", response.isStatus(), response.getAttach());
        UUID userId = (UUID) response.getAttach();
        var sessionResponse = sessionService.registerSession(userId);

        return sessionResponse.isStatus()
                ? new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }
}
