package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.isaz.prerevolutionarytinder.server.domain.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
