package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.isaz.prerevolutionarytinder.server.domain.entity.Like;

import java.util.UUID;

@Repository
public interface LikeRepository extends CrudRepository<Like, UUID> {
    boolean existsLikeByWhoAndWhom(UUID who, UUID whom);
}
