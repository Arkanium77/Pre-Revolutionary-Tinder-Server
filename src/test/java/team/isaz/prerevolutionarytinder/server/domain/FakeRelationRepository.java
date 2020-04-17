package team.isaz.prerevolutionarytinder.server.domain;

import team.isaz.prerevolutionarytinder.server.domain.entity.Relation;
import team.isaz.prerevolutionarytinder.server.domain.repository.RelationRepository;

import java.util.*;
import java.util.stream.Collectors;

public class FakeRelationRepository implements RelationRepository {
    List<Relation> rep;

    public FakeRelationRepository() {
        rep = new ArrayList<>();
    }

    @Override
    public boolean existsLikeByWhoAndWhom(UUID who, UUID whom) {
        return rep.stream()
                .anyMatch(relation ->
                        relation.getWho().equals(who)
                                && relation.getWhom().equals(whom));
    }

    @Override
    public boolean existsLikeByWhoAndWhomAndLiked(UUID who, UUID whom, boolean liked) {
        return rep.stream()
                .anyMatch(relation ->
                        relation.getWho().equals(who)
                                && relation.getWhom().equals(whom)
                                && relation.isLiked() == liked);
    }

    @Override
    public Set<Relation> findAllByWhoAndLiked(UUID who, boolean liked) {
        return rep.stream()
                .filter(relation -> relation.getWho().equals(who))
                .filter(relation -> relation.isLiked() == liked)
                .collect(Collectors.toSet());
    }

    @Override
    public <S extends Relation> S save(S s) {
        rep.add(s);
        return s;
    }

    @Override
    public <S extends Relation> Iterable<S> saveAll(Iterable<S> iterable) {
        iterable.forEach(rep::add);
        return iterable;
    }

    @Override
    public Optional<Relation> findById(UUID uuid) {
        return rep.stream()
                .filter(relation -> relation.getId().equals(uuid))
                .findAny();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return rep.stream()
                .anyMatch(relation -> relation.getId().equals(uuid));
    }

    @Override
    public Iterable<Relation> findAll() {
        return rep;
    }

    @Override
    public Iterable<Relation> findAllById(Iterable<UUID> iterable) {
        List<Relation> found = new ArrayList<>();
        for (UUID u : iterable) {
            rep.stream()
                    .filter(relation -> relation.getId().equals(u))
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
                .filter(relation -> relation.getId().equals(uuid))
                .forEach(relation -> rep.remove(relation));

    }

    @Override
    public void delete(Relation relation) {
        rep.remove(relation);
    }

    @Override
    public void deleteAll(Iterable<? extends Relation> iterable) {
        for (Relation relation : iterable) {
            rep.remove(relation);
        }
    }

    @Override
    public void deleteAll() {
        rep = new ArrayList<>();
    }
}
