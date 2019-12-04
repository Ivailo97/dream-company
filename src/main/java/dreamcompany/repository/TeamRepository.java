package dreamcompany.repository;

import dreamcompany.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {

    List<Team> findAllByProjectNull();

    Optional<Team> findByName(String name);

    List<Team> findAllByProfitAfter(BigDecimal profit);
}
