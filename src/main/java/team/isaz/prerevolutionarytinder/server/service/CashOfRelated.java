package team.isaz.prerevolutionarytinder.server.service;

import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.*;
import java.util.stream.Collectors;

public class CashOfRelated {
    Map<UUID, Stack<UUID>> cash;
    UserService userService;
    LikeService likeService;

    public CashOfRelated(UserService userService, LikeService likeService) {
        cash = new HashMap<>();
        this.userService = userService;
        this.likeService = likeService;
    }

    public Response getNext(UUID user) {
        if (!cash.containsKey(user)) cash.put(user, new Stack<>());
        var related = getStack(user);
        if (related.empty()) return new Response(false, "Нет анкет");
        return new Response(true, related.pop());
    }

    private Stack<UUID> getStack(UUID user) {
        if (cash.get(user).empty()) {
            cash.put(user, fullStack(user));
        }
        return cash.get(user);
    }

    private Stack<UUID> fullStack(UUID user) {
        List<UUID> list = userService.getRelevantUsers(user).stream()
                .filter(uuid -> likeService.isNotExistRelation(user, uuid))
                .collect(Collectors.toList());
        if (list.size() > 10) {
            list = list.subList(0, 10);
        }
        Stack<UUID> related = new Stack<>();
        list.forEach(related::push);
        return related;
    }
}
