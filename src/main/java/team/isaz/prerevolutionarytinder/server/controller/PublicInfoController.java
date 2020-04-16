package team.isaz.prerevolutionarytinder.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;
import team.isaz.prerevolutionarytinder.server.utils.DataSendingUtils;
import team.isaz.prerevolutionarytinder.server.utils.MappingUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/public_info")
public class PublicInfoController {
    Logger logger = LoggerFactory.getLogger(PublicInfoController.class);
    private UserService userService;
    private SessionService sessionService;

    public PublicInfoController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @GetMapping("/profile_info")
    public ResponseEntity<Map<String, String>> getProfileInfo(@RequestParam String uuid) {
        var mapped = MappingUtils.mappingUUID(uuid);
        if (!mapped.isStatus()) return new ResponseEntity<>(new HashMap<>(), HttpStatus.BAD_REQUEST);
        var map = userService.getUserPublicInfo((UUID) mapped.getAttach());
        if (!map.isStatus()) return new ResponseEntity<>(new HashMap<>(), HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(((Map<String, String>) map.getAttach()), HttpStatus.OK);
    }

    @GetMapping("/next_user")
    public ResponseEntity<String> getNextUserByRowNumber(@RequestParam int rowNumber) {
        var response = userService.getNextUserUUIDByNumber(rowNumber);
        return response.isStatus()
                ? new ResponseEntity<>((String) response.getAttach(), HttpStatus.OK)
                : new ResponseEntity<>((String) response.getAttach(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/setup_profile_message")
    public ResponseEntity<String> getNextRelated(@RequestBody Map<String, String> params) {
        var sessionId = MappingUtils.mappingUUID(params.get("sessionId"));
        logger.debug("Попытка сессии {} получить изменить сообщение профиля.", sessionId);

        if (!sessionId.isStatus())
            return new ResponseEntity<>("Incorrect request body. <br> " +
                    "Expected: JSON, {\"sessionId\":uuid_x,\"profile_message\":string_y}.",
                    HttpStatus.BAD_REQUEST);

        Response sessionResponse = DataSendingUtils.getSessionActivityResponse(sessionId, sessionService);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);

        var profileMessage = params.get("profile_message");
        var user = (UUID) sessionResponse.getAttach();
        var response = userService.changeProfileMessage(user, profileMessage);
        return DataSendingUtils.getOperationResponseAsResponseStringEntity(response);
    }

}
