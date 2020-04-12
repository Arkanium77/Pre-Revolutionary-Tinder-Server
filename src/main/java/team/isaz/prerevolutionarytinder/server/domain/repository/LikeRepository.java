package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.repository.CrudRepository;
import team.isaz.prerevolutionarytinder.server.domain.entities.Like;

import java.util.UUID;

public interface LikeRepository extends CrudRepository<Like, UUID> {
}
