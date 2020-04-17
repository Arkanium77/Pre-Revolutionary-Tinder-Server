package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.isaz.prerevolutionarytinder.server.domain.entity.Relation;

import java.util.Set;
import java.util.UUID;

@Repository
public interface RelationRepository extends CrudRepository<Relation, UUID> {
    boolean existsLikeByWhoAndWhom(UUID who, UUID whom);

    boolean existsLikeByWhoAndWhomAndLiked(UUID who, UUID whom, boolean liked);

    Set<Relation> findAllByWhoAndLiked(UUID who, boolean liked);

}
