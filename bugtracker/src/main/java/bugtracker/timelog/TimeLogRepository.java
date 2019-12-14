package bugtracker.timelog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeLogRepository extends JpaRepository<TimeLogEntity, Long> {
    List<TimeLogEntity> findAll();

    TimeLogEntity findTimeLogEntityById(Long id);
}
