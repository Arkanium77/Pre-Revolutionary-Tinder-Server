package team.isaz.prerevolutionarytinder.server.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.isaz.prerevolutionarytinder.server.domain.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername(String username);

    @Query(value = "select * from USERS ORDER BY ID OFFSET ? fetch next 1 row only",
            nativeQuery = true)
    User getNextUserByRowNumber(int rowNumber);

    List<User> findAllBySex(boolean sex);
}
