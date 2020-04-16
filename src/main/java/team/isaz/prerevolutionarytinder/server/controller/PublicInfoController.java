package team.isaz.prerevolutionarytinder.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team.isaz.prerevolutionarytinder.server.service.UserService;
import team.isaz.prerevolutionarytinder.server.utils.MappingUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/public_info")
public class PublicInfoController {
    Logger logger = LoggerFactory.getLogger(PublicInfoController.class);
    private UserService userService;

    public PublicInfoController(UserService userService) {
        this.userService = userService;
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

}
