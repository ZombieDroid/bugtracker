package bugtracker.history;

import javax.inject.Inject;
import java.util.List;

public class HistoryService {

    @Inject
    HistoryRepository historyRepository;

    public HistoryEntity createHistory(HistoryEntity project){
        return historyRepository.save(project);
    }
    public List<HistoryEntity> getAllByObjectId(Long objectId){
        return historyRepository.findAllByObjectId(objectId);
    }
    public HistoryEntity getHistoryById(Long id){
        return historyRepository.findById(id).orElse(null);
    }
}
