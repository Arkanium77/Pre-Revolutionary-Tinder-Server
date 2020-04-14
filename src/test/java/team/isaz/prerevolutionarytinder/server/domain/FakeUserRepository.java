package team.isaz.prerevolutionarytinder.server.domain;

import org.mindrot.jbcrypt.BCrypt;
import team.isaz.prerevolutionarytinder.server.domain.entity.User;
import team.isaz.prerevolutionarytinder.server.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("ConstantConditions")
public class FakeUserRepository implements UserRepository {
    List<User> rep;

    FakeUserRepository() {
        rep = new ArrayList<>();
    }

    public void fillRepository(int count) {
        User root = createUser("root", "1501", true, 0);
        rep.add(root);
        String girl = "Girl";
        String boy = "Boy";
        for (int i = 1; i <= count; i++) {
            if (i % 2 == 0) {
                rep.add(createUser(girl + " " + i, girl, false, i));
            } else {
                rep.add(createUser(boy + " " + i, boy, false, i));
            }
        }
    }

    @Override
    public User findByUsername(String username) {
        return rep.stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny()
                .orElseGet(null);
    }

    @Override
    public List<User> findAllBySex(boolean sex) {
        return rep.stream()
                .filter(user -> user.isSex() == sex)
                .collect(Collectors.toList());
    }

    @Override
    public <S extends User> S save(S s) {
        rep.add(s);
        return s;
    }

    @Override
    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(rep::add);
        return iterable;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        return rep.stream()
                .filter(user -> user.getId().equals(uuid))
                .findAny();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return rep.stream()
                .anyMatch(user -> user.getId().equals(uuid));
    }

    @Override
    public Iterable<User> findAll() {
        return rep;
    }

    @Override
    public Iterable<User> findAllById(Iterable<UUID> iterable) {
        List<User> found = new ArrayList<>();
        for (UUID u : iterable) {
            rep.stream()
                    .filter(user -> user.getId().equals(u))
                    .forEach(found::add);
        }
        return found;
    }

    @Override
    public long count() {
        return rep.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        rep.stream()
                .filter(user -> user.getId().equals(uuid))
                .forEach(user -> rep.remove(user));

    }

    @Override
    public void delete(User user) {
        rep.remove(user);
    }

    @Override
    public void deleteAll(Iterable<? extends User> iterable) {
        for (User u : iterable) {
            rep.remove(u);
        }
    }

    @Override
    public void deleteAll() {
        rep = new ArrayList<>();
    }

    private User createUser(String name, String password, boolean sex, int id) {
        name = name.trim();
        password = password.trim();
        User u = new User();
        u.setId(getUUID(id));
        u.setUsername(name);
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        u.setPassword(password);
        u.setRoles(0);
        u.setSex(sex);
        u.setProfileMessage("Пока здѣсь пусто...");
        return u;
    }

    public UUID getUUID(Integer i) {
        String simpleId = "00000000000000000000000000000000";
        simpleId = simpleId.substring(0, i.toString().length());
        simpleId += i.toString();
        simpleId = simpleId.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                "$1-$2-$3-$4-$5");
        return UUID.fromString(simpleId);
    }

}
