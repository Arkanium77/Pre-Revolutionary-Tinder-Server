package team.isaz.prerevolutionarytinder.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import team.isaz.prerevolutionarytinder.server.domain.Response;
import team.isaz.prerevolutionarytinder.server.service.SessionService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class DataSendingUtils {
    private final static Logger logger = LoggerFactory.getLogger(DataSendingUtils.class);

    public static Response getSessionActivityResponse(Response sessionId, SessionService sessionService) {
        return sessionService.getSessionActivity((UUID) sessionId.getAttach(), LocalDateTime.now());
    }

    public static ResponseEntity<String> getOperationResponseAsResponseStringEntity(Response response) {
        logger.debug("Response status: {}\t:::\tattach: {}", response.isStatus(), response.getAttach());
        return response.isStatus()
                ? new ResponseEntity<>(response.getAttach().toString(), HttpStatus.OK)
                : new ResponseEntity<>(response.getAttach().toString(), HttpStatus.BAD_REQUEST);
    }

    public static ResponseEntity<Map<String, String>> getOperationResponseAsResponseMapEntity(Response response) {
        logger.debug("Response status: {}\t:::\tattach: {}", response.isStatus(), response.getAttach());
        if (!response.isStatus()) return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>((Map<String, String>) response.getAttach(), HttpStatus.OK);
    }


}
