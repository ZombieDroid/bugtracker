package bugtracker.history;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {

    List<HistoryEntity> findAllByObjectId(Long objectId);
    List<HistoryEntity> findAllByProjectId(Long projectid);
}
