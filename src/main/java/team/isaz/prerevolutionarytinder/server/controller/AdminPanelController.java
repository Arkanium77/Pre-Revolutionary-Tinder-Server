package team.isaz.prerevolutionarytinder.server.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.domain.entity.User;
import team.isaz.prerevolutionarytinder.server.service.LikeService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin")
public class AdminPanelController {
    Logger logger = LoggerFactory.getLogger(AdminPanelController.class);

    private UserService userService;
    private LikeService likeService;
    private SessionService sessionService;

    @Autowired
    public AdminPanelController(UserService userService, LikeService likeService, SessionService sessionService) {
        this.userService = userService;
        this.likeService = likeService;
        this.sessionService = sessionService;
    }

    @GetMapping("/user_table")
    public ResponseEntity<String> userTable(@RequestBody Map<String, String> params) {
        return getTableAsResponseEntity(params, userService.getTable());
    }

    @GetMapping("/like_table")
    public ResponseEntity<String> likeTable(@RequestBody Map<String, String> params) {
        return getTableAsResponseEntity(params, likeService.getTable());
    }

    @GetMapping("/sessions")
    public ResponseEntity<String> sessionList(@RequestBody Map<String, String> params) {
        boolean isAdmin = checkPrivilege(params.get("sessionId"));
        if (!isAdmin) return new ResponseEntity<>("Сессия не существует или не преинадлежит администратору",
                HttpStatus.BAD_REQUEST);

        var keys = sessionService.getActiveSessions();
        var values = getValues(keys);

        var s = getStringRepresentationOfSessionList(keys, values);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

    private String getStringRepresentationOfSessionList(Set<UUID> keys, List<String> values) {
        StringBuilder s = new StringBuilder();
        var keysIterator = keys.iterator();
        var valuesIterator = values.iterator();
        while (keysIterator.hasNext() && valuesIterator.hasNext()) {
            s.append(keysIterator.next())
                    .append("    <===>    ")
                    .append(valuesIterator.next())
                    .append('\n');
        }
        return s.toString();
    }

    private List<String> getValues(Set<UUID> keys) {
        List<Object> loggedUsers = new ArrayList<>();
        keys.forEach(uuid -> loggedUsers.add(
                sessionService
                        .isSessionActive(uuid, LocalDateTime.now())
                        .getAttach()));
        return loggedUsers.stream()
                .filter(o -> o instanceof UUID)
                .map(o -> (UUID) o)
                .map(uuid -> userService.getUserById(uuid))
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    private ResponseEntity<String> getTableAsResponseEntity(Map<String, String> params, Response table) {
        boolean isAdmin = checkPrivilege(params.get("sessionId"));
        if (!isAdmin) return new ResponseEntity<>("Сессия не существует или не преинадлежит администратору",
                HttpStatus.BAD_REQUEST);

        return table.isStatus()
                ? new ResponseEntity<>(table.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(table.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }

    private boolean checkPrivilege(String sessionId) {
        boolean isAdmin = false;
        UUID sessionUUID;
        try {
            sessionUUID = UUID.fromString(sessionId);
            var session = sessionService.isSessionActive(sessionUUID, LocalDateTime.now());
            if (session.isStatus()) {
                isAdmin = userService.isAdmin((UUID) session.getAttach());
            }
        } catch (Exception e) {
            logger.info("Can't get uuid from string\n\t{}", e.getMessage());
        }
        return isAdmin;
    }


}
