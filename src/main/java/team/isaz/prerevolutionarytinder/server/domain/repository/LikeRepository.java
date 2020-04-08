package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.isaz.prerevolutionarytinder.server.domain.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
