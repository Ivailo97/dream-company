package dreamcompany.repository;

import dreamcompany.domain.entity.Office;
import dreamcompany.domain.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, String> {

    boolean existsByAddress(String address);
}
