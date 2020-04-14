package team.isaz.prerevolutionarytinder.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.CashOfRelated;
import team.isaz.prerevolutionarytinder.server.service.LikeService;
import team.isaz.prerevolutionarytinder.server.service.SessionService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/like")
public class LikeController {
    Logger logger = LoggerFactory.getLogger(LikeController.class);
    CashOfRelated cashOfRelated;
    private LikeService likeService;
    private SessionService sessionService;

    @Autowired
    public LikeController(LikeService likeService, SessionService sessionService, CashOfRelated cashOfRelated) {
        this.likeService = likeService;
        this.sessionService = sessionService;
        this.cashOfRelated = cashOfRelated;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendLike(@RequestBody Map<String, String> params) {
        var sessionId = mappingUUID(params.get("sessionId"));
        var whom = mappingUUID(params.get("whom"));
        if (!(sessionId.isStatus() && whom.isStatus()))
            return new ResponseEntity<>("Incorrect request body. <br> Expected: JSON, {\"sessionId\":uuid_x, \"whom\":uuid_y}.", HttpStatus.BAD_REQUEST);
        var sessionResponse = sessionService.isSessionActive((UUID) sessionId.getAttach(), LocalDateTime.now());
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);

        var who = (UUID) sessionResponse.getAttach();
        var response = likeService.like(who, (UUID) whom.getAttach());
        return response.isStatus()
                ? new ResponseEntity<>(response.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getNext")
    public ResponseEntity<String> getNextRelated(@RequestBody Map<String, String> params) {
        var sessionId = mappingUUID(params.get("sessionId"));
        if (!sessionId.isStatus())
            return new ResponseEntity<>("Incorrect request body. <br> Expected: JSON, {\"sessionId\":uuid_x, \"whom\":uuid_y}.", HttpStatus.BAD_REQUEST);
        var sessionResponse = sessionService.isSessionActive((UUID) sessionId.getAttach(), LocalDateTime.now());
        if (!sessionResponse.isStatus())
            return new ResponseEntity<>(sessionResponse.getAttach().toString(), HttpStatus.BAD_REQUEST);
        var user = (UUID) sessionResponse.getAttach();
        var response = cashOfRelated.getNext(user);
        return response.isStatus()
                ? new ResponseEntity<>(response.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);

    }

    private Response mappingUUID(String uuid) {
        UUID mapped = null;
        boolean status = false;
        try {
            mapped = UUID.fromString(uuid);
            status = true;
        } catch (Exception e) {
            logger.info("Can't get uuid from string \"{}\"\n\t{}", uuid, e.getMessage());
        }
        return new Response(status, mapped);
    }
}
