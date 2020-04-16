package team.isaz.prerevolutionarytinder.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.CashOfRelated;
import team.isaz.prerevolutionarytinder.server.service.RelationService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;
import team.isaz.prerevolutionarytinder.server.utils.DataSendingUtils;
import team.isaz.prerevolutionarytinder.server.utils.MappingUtils;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/relations")
public class RelationController {
    Logger logger = LoggerFactory.getLogger(RelationController.class);
    CashOfRelated cashOfRelated;
    private RelationService relationService;
    private SessionService sessionService;

    @Autowired
    public RelationController(RelationService relationService, SessionService sessionService, CashOfRelated cashOfRelated) {
        this.relationService = relationService;
        this.sessionService = sessionService;
        this.cashOfRelated = cashOfRelated;
    }

    @PostMapping("/send_relation")
    public ResponseEntity<String> sendLike(@RequestBody Map<String, String> params) {
        var sessionId = MappingUtils.mappingUUID(params.get("sessionId"));
        var whom = MappingUtils.mappingUUID(params.get("whom"));
        var isLike = MappingUtils.booleanMapping(params.get("isLike"));
        logger.debug("Попытка сессии {} создать отношение с {} со статусом isLike={}", sessionId, whom, isLike.getAttach());

        if (!(sessionId.isStatus() && whom.isStatus() && isLike.isStatus()))
            return new ResponseEntity<>(
                    "Incorrect request body. <br> " +
                            "Expected: JSON, {\"sessionId\":uuid_x, \"whom\":uuid_y, \"isLike\":bool_z}.",
                    HttpStatus.BAD_REQUEST);

        Response sessionResponse = DataSendingUtils.getSessionActivityResponse(sessionId, sessionService);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);

        var who = (UUID) sessionResponse.getAttach();
        Response response;
        if ((Boolean) isLike.getAttach()) {
            response = relationService.like(who, (UUID) whom.getAttach());
        } else {
            response = relationService.dislike(who, (UUID) whom.getAttach());
        }
        return DataSendingUtils.getOperationResponseAsResponseStringEntity(response);
    }

    @GetMapping("/getNext")
    public ResponseEntity<String> getNextRelated(@RequestBody Map<String, String> params) {
        var sessionId = MappingUtils.mappingUUID(params.get("sessionId"));
        logger.debug("Попытка сессии {} получить новую анкету.", sessionId);

        if (!sessionId.isStatus())
            return new ResponseEntity<>("Incorrect request body. <br> Expected: JSON, {\"sessionId\":uuid_x}.",
                    HttpStatus.BAD_REQUEST);

        Response sessionResponse = DataSendingUtils.getSessionActivityResponse(sessionId, sessionService);
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);

        var user = (UUID) sessionResponse.getAttach();
        var response = cashOfRelated.getNext(user);
        return DataSendingUtils.getOperationResponseAsResponseStringEntity(response);
    }

}
