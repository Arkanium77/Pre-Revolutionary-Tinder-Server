package team.isaz.prerevolutionarytinder.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.UUID;

public class MappingUtils {
    static Logger logger = LoggerFactory.getLogger(MappingUtils.class);

    public static Response booleanMapping(String bool) {
        boolean mapped;
        try {
            mapped = Boolean.parseBoolean(bool);
            logger.debug("Mapped Boolean -> {}", mapped);
        } catch (Exception e) {
            logger.debug("Can't get Boolean from string \"{}\"\n\t{}", bool, e.getMessage());
            return new Response(false, bool);
        }
        return new Response(true, mapped);
    }

    public static Response mappingUUID(String uuid) {
        UUID mapped = null;
        boolean status = false;
        try {
            mapped = UUID.fromString(uuid);
            logger.debug("Mapped UUID -> {}", mapped);
            status = true;
        } catch (Exception e) {
            logger.debug("Can't get uuid from string \"{}\"\n\t{}", uuid, e.getMessage());
        }
        return new Response(status, mapped);
    }
}
