package dreamcompany.repository;

import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByEmployeeNullAndProjectId(String id);

    List<Task> findAllByEmployeeIdAndStatus(String id, Status status);

    long countAllByStatusAndProjectId(Status status, String projectId);

    List<Task> findAllByProjectIdAndStatus(String projectId, Status status);

    List<Task> findAllByProjectId(String projectId);

    Optional<Task> findByName(String name);

    boolean existsByNameAndProjectId(String taskName, String projectId);
}
