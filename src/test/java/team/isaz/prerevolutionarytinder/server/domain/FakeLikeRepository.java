package team.isaz.prerevolutionarytinder.server.domain;

import team.isaz.prerevolutionarytinder.server.domain.entity.Like;
import team.isaz.prerevolutionarytinder.server.domain.repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeLikeRepository implements LikeRepository {
    List<Like> rep;

    FakeLikeRepository() {
        rep = new ArrayList<>();
    }

    @Override
    public boolean existsLikeByWhoAndWhom(UUID who, UUID whom) {
        return rep.stream()
                .anyMatch(like ->
                        like.getWho().equals(who)
                                && like.getWhom().equals(whom));
    }

    @Override
    public boolean existsLikeByWhoAndWhomAndLiked(UUID who, UUID whom, boolean liked) {
        return rep.stream()
                .anyMatch(like ->
                        like.getWho().equals(who)
                                && like.getWhom().equals(whom)
                                && like.isLiked() == liked);
    }

    @Override
    public <S extends Like> S save(S s) {
        rep.add(s);
        return s;
    }

    @Override
    public <S extends Like> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(rep::add);
        return iterable;
    }

    @Override
    public Optional<Like> findById(UUID uuid) {
        return rep.stream()
                .filter(like -> like.getId().equals(uuid))
                .findAny();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return rep.stream()
                .anyMatch(like -> like.getId().equals(uuid));
    }

    @Override
    public Iterable<Like> findAll() {
        return rep;
    }

    @Override
    public Iterable<Like> findAllById(Iterable<UUID> iterable) {
        List<Like> found = new ArrayList<>();
        for (UUID u : iterable) {
            rep.stream()
                    .filter(like -> like.getId().equals(u))
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
                .filter(like -> like.getId().equals(uuid))
                .forEach(like -> rep.remove(like));

    }

    @Override
    public void delete(Like like) {
        rep.remove(like);
    }

    @Override
    public void deleteAll(Iterable<? extends Like> iterable) {
        for (Like like : iterable) {
            rep.remove(like);
        }
    }

    @Override
    public void deleteAll() {
        rep = new ArrayList<>();
    }
}
