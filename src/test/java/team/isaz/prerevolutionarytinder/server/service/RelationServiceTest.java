package team.isaz.prerevolutionarytinder.server.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import team.isaz.prerevolutionarytinder.server.domain.FakeRelationRepository;
import team.isaz.prerevolutionarytinder.server.domain.FakeUserRepository;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.UUID;


class RelationServiceTest {
    RelationService relationService;
    FakeUserRepository users;

    @BeforeEach
    void init() {
        var rep = new FakeRelationRepository();
        relationService = new RelationService(rep);
        users = new FakeUserRepository();
        users.fillRepository(5);
    }

    @ParameterizedTest
    @CsvSource({
            "00000000-0000-0000-0000-000000000000, 00000000-0000-0000-0000-000000000002",
            "00000000-0000-0000-0000-000000000000, 00000000-0000-0000-0000-000000000004",
            "00000000-0000-0000-0000-000000000001, 00000000-0000-0000-0000-000000000002",
            "00000000-0000-0000-0000-000000000002, 00000000-0000-0000-0000-000000000001"
    })
    void simpleLike(UUID who, UUID whom) {
        var response = new Response(true, "Любовь проявлена");
        Assertions.assertThat(relationService.like(who, whom)).isEqualTo(response);
    }

    @Test
    void likeWithMatches() {
        UUID root = UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID boy = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID girl = UUID.fromString("00000000-0000-0000-0000-000000000002");
        var response = new Response(true, "Любовь проявлена");
        var response1 = new Response(true, "Вы любимы");
        var response2 = new Response(false, "Дубликатъ");
        var response3 = new Response(true, "Видно не судьба, видно нѣтъ любви.");

        Assertions.assertThat(relationService.like(root, girl)).isEqualTo(response);
        Assertions.assertThat(relationService.like(girl, root)).isEqualTo(response1);
        Assertions.assertThat(relationService.like(root, girl)).isEqualTo(response2);
        Assertions.assertThat(relationService.dislike(root, girl)).isEqualTo(response2);
        Assertions.assertThat(relationService.dislike(boy, root)).isEqualTo(response3);
    }


    @Test
    void isNotExistRelation() {
        UUID root = UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID boy = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID girl = UUID.fromString("00000000-0000-0000-0000-000000000002");
        relationService.like(root, girl);
        relationService.like(girl, root);
        relationService.like(root, boy);
        Assertions.assertThat(relationService.isNotExistRelation(root, girl)).isFalse();
        Assertions.assertThat(relationService.isNotExistRelation(boy, girl)).isTrue();
        Assertions.assertThat(relationService.isNotExistRelation(boy, root)).isFalse();
    }
}