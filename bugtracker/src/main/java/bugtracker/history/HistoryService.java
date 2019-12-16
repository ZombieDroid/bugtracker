package bugtracker.history;

import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class HistoryService {

    @Inject
    HistoryRepository historyRepository;

    public HistoryEntity createHistory(HistoryEntity historyEntity){
        return historyRepository.save(historyEntity);
    }
    public List<HistoryEntity> getAllByObjectId(Long objectId){
        return historyRepository.findAllByObjectId(objectId);
    }
    public List<HistoryEntity> getAllByProjectId(Long projectId){
        return historyRepository.findAllByProjectId(projectId);
    }
    public HistoryEntity getHistoryById(Long id){
        return historyRepository.findById(id).orElse(null);
    }
}
