package dreamcompany.repository;

import dreamcompany.domain.entity.Project;
import dreamcompany.domain.enumeration.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    List<Project> findAllByStatus(Status status);

    List<Project> findAllByStatusNotIn(Status... statuses);

    Optional<Project> findByName(String name);

    boolean existsByName(String name);
}
