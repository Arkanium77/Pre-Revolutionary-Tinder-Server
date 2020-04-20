package team.isaz.prerevolutionarytinder.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <b>Инструмент кеширования предлагаемых анкет.</b><br>
 * Необходим для уменьшения обращений к БД
 */
public class CashOfRelated {
    private final int CASH_SIZE = 10;
    final Map<UUID, Stack<UUID>> cash;
    final UserService userService;
    final RelationService relationService;
    private final Logger logger = LoggerFactory.getLogger(CashOfRelated.class);

    public CashOfRelated(UserService userService, RelationService relationService) {
        cash = new HashMap<>();
        this.userService = userService;
        this.relationService = relationService;
    }

    /**
     * <b>Получить следующую анкету</b><br>
     * Извлекает анкету из внутреннего кеша.
     *
     * @param user пользователь, для которого мы извлекаем анкету.
     * @return UUID подходящей анкеты.
     */
    public Response getNext(UUID user) {
        if (!cash.containsKey(user)) cash.put(user, new Stack<>());
        var related = getStack(user);
        if (related.empty()) return new Response(false, "Нет анкет");

        logger.debug("Извлечена анкета {}, в кеше осталось {} элементов", related.peek(), related.size());
        return new Response(true, related.pop());
    }

    /**
     * <b>Получить кеш</b><br>
     *
     * @param user пользователь, ассоциированный с кешем.
     * @return непустой Stack<UUID> содержащий только подходящие анкеты.
     */
    private Stack<UUID> getStack(UUID user) {
        if (cash.get(user).empty()) {
            logger.debug("Кеш пользователя {} опустел", user);
            cash.put(user, fullStack(user));
        }
        return cash.get(user);
    }


    /**
     * <b>Пополнить кеш</b><br>
     * Выполняется когда кеш опустеет. Операция крайне не быстрая.
     *
     * @param user пользователей, для которого осуществляется поиск анкет.
     * @return Stack<UUID> заданного размера
     */
    private Stack<UUID> fullStack(UUID user) {
        List<UUID> list = userService.getRelevantUsers(user).stream()
                .filter(uuid -> relationService.isNotExistRelation(user, uuid))
                .collect(Collectors.toList());
        if (list.size() > CASH_SIZE) {
            list = list.subList(0, CASH_SIZE);
        }
        Stack<UUID> related = new Stack<>();
        list.forEach(related::push);
        logger.debug("Кеш пополнен ({} элементов)", related.size());
        return related;
    }

    public int getCashSize() {
        return CASH_SIZE;
    }
}
