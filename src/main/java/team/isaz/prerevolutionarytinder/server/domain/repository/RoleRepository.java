package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.isaz.prerevolutionarytinder.server.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
