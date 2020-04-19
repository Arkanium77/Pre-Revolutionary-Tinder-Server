package team.isaz.prerevolutionarytinder.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.CashOfRelated;
import team.isaz.prerevolutionarytinder.server.service.RelationService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.service.UserService;
import team.isaz.prerevolutionarytinder.server.utils.DataSendingUtils;
import team.isaz.prerevolutionarytinder.server.utils.MappingUtils;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/relations")
public class RelationController {
    Logger logger = LoggerFactory.getLogger(RelationController.class);
    CashOfRelated cashOfRelated;
    private final RelationService relationService;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public RelationController(RelationService relationService, SessionService sessionService,
                              CashOfRelated cashOfRelated, UserService userService) {
        this.relationService = relationService;
        this.sessionService = sessionService;
        this.cashOfRelated = cashOfRelated;
        this.userService = userService;
    }

    @PostMapping("/send_relation")
    public ResponseEntity<String> sendLike(@RequestBody Map<String, String> params) {
        var sessionId = MappingUtils.mappingUUID(params.get("session_id"));
        var whom = MappingUtils.mappingUUID(params.get("whom"));
        var isLike = MappingUtils.booleanMapping(params.get("is_like"));
        logger.debug("Попытка сессии {} создать отношение с {} со статусом is_like={}", sessionId, whom, isLike.getAttach());

        if (!(sessionId.isStatus() && whom.isStatus() && isLike.isStatus()))
            return new ResponseEntity<>(
                    "Incorrect request body. <br> " +
                            "Expected: JSON, {\"session_id\":uuid_x, \"whom\":uuid_y, \"is_like\":bool_z}.",
                    HttpStatus.BAD_REQUEST);

        Response sessionResponse = DataSendingUtils.getSessionActivityResponse(sessionId, sessionService);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.UNAUTHORIZED);

        var who = (UUID) sessionResponse.getAttach();
        Response response;
        if ((Boolean) isLike.getAttach()) {
            response = relationService.like(who, (UUID) whom.getAttach());
        } else {
            response = relationService.dislike(who, (UUID) whom.getAttach());
        }
        return DataSendingUtils.getOperationResponseAsResponseStringEntity(response);
    }

    @PostMapping("/get_next")
    public ResponseEntity<String> getNextRelated(@RequestBody Map<String, String> params) {
        var sessionId = MappingUtils.mappingUUID(params.get("session_id"));
        logger.debug("Попытка сессии {} получить новую анкету.", sessionId);

        if (!sessionId.isStatus())
            return new ResponseEntity<>("Incorrect request body. <br> Expected: JSON, {\"session_id\":uuid_x}.",
                    HttpStatus.BAD_REQUEST);

        Response sessionResponse = DataSendingUtils.getSessionActivityResponse(sessionId, sessionService);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.UNAUTHORIZED);

        var user = (UUID) sessionResponse.getAttach();
        var response = cashOfRelated.getNext(user);
        return DataSendingUtils.getOperationResponseAsResponseStringEntity(response);
    }

    @PostMapping("/get_all_matches")
    public ResponseEntity<Map<String, String>> getAllMatches(@RequestBody Map<String, String> params) {
        var sessionId = MappingUtils.mappingUUID(params.get("session_id"));
        logger.debug("Попытка сессии {} получить новую анкету.", sessionId);

        if (!sessionId.isStatus())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        Response sessionResponse = DataSendingUtils.getSessionActivityResponse(sessionId, sessionService);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        var user = (UUID) sessionResponse.getAttach();
        var uuids = relationService.getAllMatchesById(user);
        var names = userService.getNamesById(uuids);
        var response = MappingUtils.zipToStringMap(uuids, names);
        return DataSendingUtils.getOperationResponseAsResponseMapEntity(response);
    }

}
