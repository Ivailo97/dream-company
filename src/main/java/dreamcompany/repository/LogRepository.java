package dreamcompany.repository;

import dreamcompany.domain.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {

    @Query("SELECT l FROM Log l ORDER BY l.createdOn DESC")
    List<Log> findAllOrderedByDateDesc();

    List<Log> findAllByUsername(String username);
}
