package dreamcompany.repository;

import dreamcompany.domain.entity.Position;
import dreamcompany.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    List<User> findAllByTeamNullAndPositionNotIn(Position... positions);

    List<User> findAllByTeamId(String id);

    List<User> findAllByTeamIdAndPosition(String id, Position position);

    //
    List<User> findAllByTeamNullAndPosition(Position position);

    List<User> findAllByPosition(Position position);

    List<User> findAllByPositionNotIn(Position... positions);

    //

    List<User> findAllByCreditsGreaterThanAndPositionNotIn(Integer credits, Position... positions);

    Optional<User> findByEmail(String email);
}
