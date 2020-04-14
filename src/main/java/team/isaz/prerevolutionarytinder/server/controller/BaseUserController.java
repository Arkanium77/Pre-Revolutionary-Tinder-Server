package team.isaz.prerevolutionarytinder.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.LikeService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("users")
public class BaseUserController {
    private UserService userService;
    private LikeService likeService;
    private SessionService sessionService;

    @Autowired
    public BaseUserController(UserService userService, LikeService likeService, SessionService sessionService) {
        this.userService = userService;
        this.likeService = likeService;
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username,
                                        @RequestParam String password) {
        var response = userService.login(username, password);
        return getSessionCreateResponseEntity(response);
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username,
                                           @RequestParam String password,
                                           @RequestParam boolean sex) {
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

    @GetMapping("/utable")
    public ResponseEntity<String> utable() {
        var response = userService.getTable();
        return response.isStatus()
                ? new ResponseEntity<>(response.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/like")
    public ResponseEntity<String> like(@RequestParam UUID sessionId,
                                       @RequestParam UUID whom) {
        var sessionResponse = sessionService.isSessionActive(sessionId);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);

        var who = (UUID) sessionResponse.getAttach();
        var response = likeService.like(who, whom);
        return response.isStatus()
                ? new ResponseEntity<>(response.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/ltable")
    public ResponseEntity<String> ltable() {
        var response = likeService.getTable();
        return response.isStatus()
                ? new ResponseEntity<>(response.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }


}
