package team.isaz.prerevolutionarytinder.server.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MappingUtils {
    private final static Logger logger = LoggerFactory.getLogger(MappingUtils.class);

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

    public static Response zipToStringMap(List<?> keys, List<?> values) {
        if (keys.size() != values.size()) return new Response(false, "Длины списков не совпадают");
        var map = new HashMap<String, String>();
        var kIterator = keys.stream().map(Object::toString).collect(Collectors.toList()).iterator();
        var vIterator = values.stream().map(Object::toString).collect(Collectors.toList()).iterator();
        while (kIterator.hasNext() && vIterator.hasNext()) {
            map.put(kIterator.next(), vIterator.next());
        }
        return new Response(true, map);
    }
}
