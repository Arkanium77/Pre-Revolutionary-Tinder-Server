package team.isaz.prerevolutionarytinder.server.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import team.isaz.prerevolutionarytinder.server.domain.FakeUserRepository;
import team.isaz.prerevolutionarytinder.server.domain.entity.User;

import java.util.UUID;

class UserServiceTest {
    UserService service;

    @BeforeEach
    void init() {
        var rep = new FakeUserRepository();
        rep.fillRepository(5);
        service = new UserService(rep);
    }

    @ParameterizedTest
    @CsvSource({
            "Helen, password, false",
            "Andrew, pA55w0Rd,true",
            "Jane, &?1$^°□°）,true"
    })
    void correctRegister(String username, String password, Boolean sex) {
        var r = service.register(username, password, sex);
        Assertions.assertThat(r.isStatus()).isEqualTo(true);
    }

    @ParameterizedTest
    @CsvSource({
            "Helen, , false",
            " , pA55w0Rd,true",
            "Jane, &?1$^°□°）,"
    })
    void incorrectRegister(String username, String password, Boolean sex) {
        var r = service.register(username, password, sex);
        Assertions.assertThat(r.isStatus()).isEqualTo(false);
    }

    @Test
    void login() {
        Assertions.assertThat(service.login("root", "1501").isStatus()).isTrue();
        Assertions.assertThat(service.login("Boy 1", "Boy").isStatus()).isTrue();
        Assertions.assertThat(service.login("Boy 1", "Girl").isStatus()).isFalse();
    }

    @Test
    void isAdmin() {
        Assertions.assertThat(service.isAdmin(service.userRepository.findByUsername("root").getId()))
                .isTrue();
        Assertions.assertThat(service.isAdmin(service.userRepository.findByUsername("Boy 1").getId()))
                .isFalse();
    }

    @Test
    void getUserById() {
        UUID rootId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        UUID boyId = UUID.fromString("00000000-0000-0000-0000-000000000001");
        var iterator = service.userRepository.findAll().iterator();

        User root = service.getUserById(rootId);
        Assertions.assertThat(root).isEqualTo(iterator.next());

        User boy = service.getUserById(boyId);
        Assertions.assertThat(boy).isEqualTo(iterator.next());
    }

    @Test
    void getRelevantUsers() {
        UUID rootId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        var relevantUsers = service.getRelevantUsers(rootId);
        relevantUsers.forEach(uuid -> Assertions.assertThat(service.getUserById(uuid).isSex()).isFalse());

        UUID girlId = UUID.fromString("00000000-0000-0000-0000-000000000002");
        relevantUsers = service.getRelevantUsers(girlId);
        relevantUsers.forEach(uuid -> Assertions.assertThat(service.getUserById(uuid).isSex()).isTrue());
    }
}