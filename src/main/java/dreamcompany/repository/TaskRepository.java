package dreamcompany.repository;

import dreamcompany.domain.entity.Status;
import dreamcompany.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByEmployeeNullAndProjectId(String id);

    List<Task> findAllByEmployeeIdAndStatus(String id, Status status);

    long countAllByStatusAndProjectId(Status status, String projectId);

}
