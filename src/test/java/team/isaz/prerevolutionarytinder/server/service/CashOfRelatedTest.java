package team.isaz.prerevolutionarytinder.server.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.isaz.prerevolutionarytinder.server.domain.FakeRelationRepository;
import team.isaz.prerevolutionarytinder.server.domain.FakeUserRepository;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.UUID;

class CashOfRelatedTest {
    static CashOfRelated cash;
    Logger logger = LoggerFactory.getLogger(CashOfRelatedTest.class);

    @BeforeAll
    static void init() {
        FakeRelationRepository likes = new FakeRelationRepository();
        FakeUserRepository users = new FakeUserRepository();
        users.fillRepository(100);
        UserService userService = new UserService(users);
        RelationService relationService = new RelationService(likes);
        cash = new CashOfRelated(userService, relationService);
    }

    @Test
    void countOf() {
        UUID root = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Assertions.assertThat(cash.userService.getRelevantUsers(root).size()).isEqualTo(50);
    }

    @RepeatedTest(50)
    void stackUsage() {
        UUID root = UUID.fromString("00000000-0000-0000-0000-000000000000");
        int cashedSize = cash.getCashSize();
        try {
            cashedSize = cash.cash.get(root).size();
            logger.debug("cash size is {}", cashedSize);
        } catch (NullPointerException e) {
            Assertions.assertThat(cash.cash.size()).isEqualTo(0);
            logger.debug("cash isn't created");
        }
        Response r = cash.getNext(root);
        if (cashedSize == 0) {
            Assertions.assertThat(cash.cash.get(root).size()).isEqualTo(cash.getCashSize() - 1);
        } else {
            Assertions.assertThat(cash.cash.get(root).size()).isEqualTo(cashedSize - 1);
        }

        Assertions.assertThat(r.isStatus()).isTrue();
        cash.relationService.like(root, (UUID) r.getAttach());
    }


}