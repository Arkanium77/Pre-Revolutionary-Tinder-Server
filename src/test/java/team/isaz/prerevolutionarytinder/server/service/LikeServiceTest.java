package team.isaz.prerevolutionarytinder.server.service;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import team.isaz.prerevolutionarytinder.server.domain.FakeLikeRepository;
import team.isaz.prerevolutionarytinder.server.domain.FakeUserRepository;
import team.isaz.prerevolutionarytinder.server.domain.Response;

import java.util.UUID;


class LikeServiceTest {
    LikeService likeService;
    FakeUserRepository users;

    @BeforeEach
    void init() {
        var rep = new FakeLikeRepository();
        likeService = new LikeService(rep);
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
        Assertions.assertThat(likeService.like(who, whom)).isEqualTo(response);
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

        Assertions.assertThat(likeService.like(root, girl)).isEqualTo(response);
        Assertions.assertThat(likeService.like(girl, root)).isEqualTo(response1);
        Assertions.assertThat(likeService.like(root, girl)).isEqualTo(response2);
        Assertions.assertThat(likeService.dislike(root, girl)).isEqualTo(response2);
        Assertions.assertThat(likeService.dislike(boy, root)).isEqualTo(response3);
    }


    @Test
    void isNotExistRelation() {
        UUID root = UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID boy = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID girl = UUID.fromString("00000000-0000-0000-0000-000000000002");
        likeService.like(root, girl);
        likeService.like(girl, root);
        likeService.like(root, boy);
        Assertions.assertThat(likeService.isNotExistRelation(root, girl)).isFalse();
        Assertions.assertThat(likeService.isNotExistRelation(boy, girl)).isTrue();
        Assertions.assertThat(likeService.isNotExistRelation(boy, root)).isFalse();
    }
}