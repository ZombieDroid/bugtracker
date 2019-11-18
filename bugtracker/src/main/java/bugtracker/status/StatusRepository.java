package bugtracker.status;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    List<StatusEntity> findAll();
    StatusEntity findStatusById(Long id);
    List<StatusEntity> findByNameContaining(String text);
}
